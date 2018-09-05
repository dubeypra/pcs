/**
 * 
 */
package verizon.entity;

import com.ciena.rm.uinavigation.model.UIContainer;

/**
 * @author prdubey
 *
 */
public class UIContainerWrapper {
    private int parentId;
    private int annotationFlag;
    private long annotationID;
    private UIContainer container;

   
    public UIContainer getContainer() {
        return container;
    }

    public void setContainer(UIContainer container) {
        this.container = container;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public int getAnnotationFlag() {
        return annotationFlag;
    }

    public void setAnnotationFlag(int annotationFlag) {
        this.annotationFlag = annotationFlag;
    }

    public long getAnnotationID() {
        return annotationID;
    }

    public void setAnnotationID(long annotationID) {
        this.annotationID = annotationID;
    }

    @Override
    public String toString() {
	return "UIContainerWrapper [parentId=" + parentId + ", annotationFlag=" + annotationFlag + ", annotationID=" + annotationID + ", container=" + container + "]";
    }
    
    

}
