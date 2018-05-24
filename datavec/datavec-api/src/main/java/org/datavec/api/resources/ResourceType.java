package org.datavec.api.resources;

public enum ResourceType {

    DATASET,
    RESOURCE;

    public String resourceName(){
        switch (this){
            case DATASET:
                return "data";
            case RESOURCE:
                return "resources";
            default:
                return this.toString();
        }
    }

}
