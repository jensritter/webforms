package org.jens.webforms.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Basis-Pojo für alle Elemente.
 * <p>
 * Nur eine teilmenge von Properties werden direkt nach Json gemappt. Viele Properties werden via JsonForm zu einem ElementForm umgewandelt
 * <p>
 * https://jsonform.github.io/jsonform/playground/index.html?example=schema-basic
 * https://github.com/jsonform/jsonform
 * <p>
 * TODO: https://github.com/jsonform/jsonform/wiki#multiple-options-the-checkboxes-type
 * TODO: https://github.com/jsonform/jsonform/wiki#a-list-of-radio-buttons-the-radios-type
 * TODO: https://github.com/jsonform/jsonform/wiki#group-of-buttons-the-actions-type
 * https://github.com/jsonform/jsonform/wiki#common-schema-properties
 *
 * @author Jens Ritter on 29/08/2021.
 * @see JsonForm
 */
@SuppressWarnings({"NegativelyNamedBooleanVariable", "WeakerAccess"})
@JsonInclude(Include.NON_DEFAULT)
public abstract class ElementSchema<T> {
    private final Logger logger = LoggerFactory.getLogger(ElementSchema.class);

    private final FormType type;

    private String title;
    private boolean required;
    @Nullable private String description;
    @Nullable private Object defaultValue;

    /* form-properties */
    private boolean notitle;
    private boolean disabled;
    private boolean readonly;
    @Nullable private String htmlClass;
    @Nullable private String fieldHtmlClass;
    @Nullable private String prepend;
    @Nullable private String append;
    @Nullable private String placeholder;


    /**
     * Available types
     *
     * @see <a href="https://github.com/jsonform/jsonform/wiki#schema-supported">https://github.com/jsonform/jsonform/wiki#schema-supported</a>
     */
    enum FormType {
        FormString("string"),
        FormNumber("number"),
        FormInteger("integer"),
        FormBoolean("boolean"),
        FormArray("array"),
        FormObject("object");

        private final String name;

        FormType(String name) {
            this.name = name;
        }

        @JsonValue
        public String toString() {
            return name;
        }
    }

    protected ElementSchema(FormType type, String label) {
        this.type = type;
        this.title = label;
    }

    /**
     * Creates an ElementForm for the Control
     *
     * @param element Default-Element
     */
    protected abstract void buildForm(ElementForm element);

    void buildDefaultForm(ElementForm element) {
        element.setNoTitle(notitle);
        element.setHtmlClass(htmlClass);
        element.setFieldHtmlClass(fieldHtmlClass);

        element.setPrepend(prepend);
        element.setAppend(append);
        element.setPlaceholder(placeholder);
        element.setDisabled(disabled);
        element.setReadonly(readonly);
    }

    /**
     * Method to set the {@link #defaultValue} per Element
     *
     * @param value
     * @return
     */
    public abstract ElementSchema<T> value(@Nullable T value);

    /**
     * Parse the "default"-Value and aother component-specific properties
     *
     * @param schemaElement
     * @param formElement
     * @param defaultValueNode
     */
    protected abstract void parseForm(JsonNode schemaElement, JsonNode formElement, Optional<JsonNode> defaultValueNode);

    /**
     * Parse default properties for all elements
     *
     * @param schemaElement
     * @param formElement
     */
    void parseFormDefaults(JsonNode schemaElement, JsonNode formElement) {
        String schemaTitle = parseValueAsString(schemaElement, "title");
        if(schemaTitle != null) {
            setTitle(schemaTitle);
        } else {
            logger.warn("Element does not have a title {}", schemaElement);
            setTitle("");
        }

        setRequired(parseValueAsBoolean(schemaElement, "required"));
        setDescription(parseValueAsString(schemaElement, "description"));
//        setDefaultValue(getValueAsString(schemaElement, "default"));
        setNotitle(parseValueAsBoolean(formElement, "notitle"));
        setDisabled(parseValueAsBoolean(formElement, "disabled"));
        setReadonly(parseValueAsBoolean(formElement, "readonly"));
        setFieldHtmlClass(parseValueAsString(formElement, "fieldHtmlClass"));
        setHtmlClass(parseValueAsString(formElement, "htmlClass"));
        setPrepend(parseValueAsString(formElement, "prepend"));
        setAppend(parseValueAsString(formElement, "append"));
        setPlaceholder(parseValueAsString(formElement, "placeholder"));
    }

    //
    // Helper-methods
    //
    boolean parseValueAsBoolean(JsonNode schemaElement, String key) {
        JsonNode entry = schemaElement.get(key);
        return entry != null && entry.asBoolean();
    }

    @Nullable String parseValueAsString(JsonNode node, String key) {
        JsonNode jsonNode = node.get(key);
        if(jsonNode != null) {
            return jsonNode.asText();
        }
        return null;
    }


    //
    // bean-methods
    //

    public FormType getType() {return type;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public void setRequired(boolean required) {this.required = required;}

    public boolean isRequired() {return this.required;}

    public @Nullable String getDescription() {return description;}

    public void setDescription(@Nullable String description) {this.description = description;}

    @JsonProperty("default")
    @Nullable
    public Object getDefaultValue() {return defaultValue;}

    ElementSchema<T> setDefaultValue(@Nullable Object opt) {
        this.defaultValue = opt;
        return this;
    }

    @JsonIgnore // property not in schema
    public boolean isNoTitle() {return this.notitle;}

    @JsonIgnore // Property not in schema
    @Nullable
    public String getHtmlClass() {return htmlClass;}

    public void setHtmlClass(@Nullable String htmlClass) {this.htmlClass = htmlClass;}

    public void setNotitle(boolean value) {this.notitle = value;}

    @JsonIgnore // Property not in schema
    @Nullable
    public String getFieldHtmlClass() {return fieldHtmlClass;}

    public void setFieldHtmlClass(@Nullable String fieldHtmlClass) {this.fieldHtmlClass = fieldHtmlClass;}

    @JsonIgnore // Property not in schema
    @Nullable
    public String getPrepend() {return prepend;}

    public void setPrepend(@Nullable String prepend) {this.prepend = prepend;}

    @JsonIgnore // Property not in schema
    @Nullable
    public String getAppend() {return append;}

    public void setAppend(@Nullable String append) {this.append = append;}

    @JsonIgnore // Property not in schema
    @Nullable
    public String getPlaceholder() {return placeholder;}

    public void setPlaceholder(@Nullable String placeholder) {this.placeholder = placeholder;}

    @JsonIgnore // Property not in schema
    public boolean isDisabled() {return disabled;}

    public void setDisabled(boolean disabled) {this.disabled = disabled;}

    @JsonIgnore // Property not in schema
    public boolean isReadonly() {return readonly;}

    public void setReadonly(boolean readonly) {this.readonly = readonly;}


    //
    // builders
    //

    public ElementSchema<T> title(String value) {
        setTitle(value);
        return this;
    }

    public ElementSchema<T> required(boolean value) {
        setRequired(value);
        return this;
    }

    public ElementSchema<T> description(String value) {
        setDescription(value);
        return this;
    }

    public ElementSchema<T> noTitle(boolean value) {
        setNotitle(value);
        return this;
    }

    public ElementSchema<T> htmlClass(String value) {
        setHtmlClass(value);
        return this;
    }

    public ElementSchema<T> fieldHtmlClass(String value) {
        setFieldHtmlClass(value);
        return this;
    }

    public ElementSchema<T> prepend(String value) {
        setPrepend(value);
        return this;
    }

    public ElementSchema<T> append(String value) {
        setAppend(value);
        return this;
    }

    public ElementSchema<T> placeholder(String value) {
        setPlaceholder(value);
        return this;
    }

    public ElementSchema<T> disabled(boolean value) {
        setDisabled(value);
        return this;
    }

    public ElementSchema<T> readOnly(boolean value) {
        setReadonly(value);
        return this;
    }


}
