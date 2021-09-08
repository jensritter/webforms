package org.jens.webforms.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jens Ritter on 07/09/2021.
 */
class JsonFormParserTest {

    @BeforeEach
    public void setUpForm() {
        jsonForm = new JsonForm();
        parser = new JsonFormParser();
    }

    JsonForm jsonForm;
    JsonFormParser parser;

    @Test
    void parseElements() throws JsonProcessingException {
        List<ElementSchema<?>> elementSchemas = Arrays.asList(
            new FBoolean("welcome"),
            new FString("welcome"),
//            new FArray("welcome"),
            new FComboBox("welcome").selectionValue("10", "10"),
            new FDate("welcome"),
            new FInteger("welcome"),
            new FNumber("welcome"),
//            new FObject("welcome"),
            new FRange("welcome"),
            new FString("welcome"),
            new FTextArea("welcome")
        );
        for(ElementSchema<?> loop : elementSchemas) {
            jsonForm = new JsonForm();

            loop
                .placeholder("placeholder")
                .required(true)
                .disabled(true)
                .fieldHtmlClass("fieldhtml")
                .htmlClass("htmlclass")
                .noTitle(true)
                .prepend("prepend")
                .append("append")
                .description("description")
                .readOnly(true);
            jsonForm.add("welcome", loop);

            Map<String, ElementSchema<?>> elements = parser.parseElements(jsonForm.toString());
            assertThat(elements).hasSize(1);
            assertThat(elements).containsOnlyKeys("welcome");

            ElementSchema<?> element = elements.get("welcome");
            assertThat(element).isNotNull();
            assertThat(element.getPlaceholder()).describedAs("placeholder missing from " + element).isEqualTo("placeholder");
            assertThat(element.isRequired()).describedAs("required missing from" + element).isEqualTo(true);
            assertThat(element.isDisabled()).describedAs("disabled missing from" + element).isEqualTo(true);
            assertThat(element.getFieldHtmlClass()).describedAs("FieldHtmlClass missing from" + element).isEqualTo("fieldhtml");
            assertThat(element.getHtmlClass()).describedAs("htmlClass missing from" + element).isEqualTo("htmlclass");
            assertThat(element.isNoTitle()).describedAs("notitle missing from" + element).isTrue();
            assertThat(element.getPrepend()).describedAs("prepend missing from" + element).isEqualTo("prepend");
            assertThat(element.getAppend()).describedAs("append missing from" + element).isEqualTo("append");
            assertThat(element.getDescription()).describedAs("description missing from" + element).isEqualTo("description");
            assertThat(element.isReadonly()).describedAs("readonly missing from" + element).isTrue();
        }


    }

    @Test
    public void testBoolean() throws JsonProcessingException {
        jsonForm.add("bool", new FBoolean("title").inlineTitle("inlineTitle").value(true));
        Map<String, ElementSchema<?>> elements = parser.parseElements(jsonForm.toString());

        assertThat(elements).containsOnlyKeys("bool");
        ElementSchema<?> bool = elements.get("bool");
        assertThat(bool).isNotNull();

        assertThat(bool).isInstanceOf(FBoolean.class);
        FBoolean fBoolean = (FBoolean) bool;

        assertThat(fBoolean.getInlineTitle()).isEqualTo("inlineTitle");
        assertThat(fBoolean.getDefaultValue()).isEqualTo(Boolean.TRUE);
    }
}
