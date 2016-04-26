/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2014 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.myee.tarot.menu.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Generic DTO object that represents the information to display a MenuService Item.
 * Can be used on the front end as a way to easily iterate and display menu items of different types.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class MenuItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String label;
    protected String url;
    protected String imageUrl;
    protected String altText;
    protected List<MenuItemDTO> submenu = new ArrayList<MenuItemDTO>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public List<MenuItemDTO> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<MenuItemDTO> submenu) {
        this.submenu = submenu;
    }

}
