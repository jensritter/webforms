package org.jens.webforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jens Ritter on 06/09/2021.
 */
class FArrayTest extends JsonTester {

    static final String PLAIN_SCHEMA = "{\"type\":\"array\",\"title\":\"label\"}";

    FArray element;

    @BeforeEach
    public void setupElement() {
        element = new FArray("label");
    }

    @Override
    @Test
    public void testJson() throws JsonProcessingException {
        assertThat(toSchemaJson(element)).isEqualTo(PLAIN_SCHEMA);
        assertThat(toFormJson(element)).isEqualTo(PLAIN_FORM);
    }


}
