/**
 * 
 */
package verizon.entity;

import com.ciena.rm.uinavigation.model.UINode;

/**
 * @author prdubey
 *
 */
public class UINodeWrapper {
    
    private UINode uiNode;
    private long annotationId;
    private long nodeId;
    public UINode getUiNode() {
        return uiNode;
    }
    public void setUiNode(UINode uiNode) {
        this.uiNode = uiNode;
    }
    public long getAnnotationId() {
        return annotationId;
    }
    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }
    public long getNodeId() {
        return nodeId;
    }
    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }
    
    

}
