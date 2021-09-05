
package org.jens.webforms.core.controller.form;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Jens Ritter on 29/08/2021.
 */
public class FBoolean extends ElementControl {

    private String inlineTitle;

    private FBoolean(String label) {
        super(FormType.FormBoolean, label);
    }

    public FBoolean(String label, String inlinetitle) {
        this(label);
        this.inlineTitle = inlinetitle;
    }

    @JsonIgnore
    public String getInlineTitle() {return inlineTitle;}

    public void setInlineTitle(String inlineTitle) {this.inlineTitle = inlineTitle;}

    @Override
    ElementForm buildForm(ElementForm element) {
        element.setInlinetitle(inlineTitle);
        return element;
    }
}
