package com.oleg.hubal.topfour.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12.12.2016.
 */

public class Photo {
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("suffix")
    @Expose
    private String suffix;

    /**
     *
     * @return
     * The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     *
     * @param prefix
     * The prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     *
     * @return
     * The suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     *
     * @param suffix
     * The suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
