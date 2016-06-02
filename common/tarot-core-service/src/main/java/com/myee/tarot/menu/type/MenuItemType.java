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
package com.myee.tarot.menu.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * MenuService Item Types
 *
 * @author bpolster
 *
 */
public class  MenuItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<String, MenuItemType> TYPES = new HashMap<String, MenuItemType>();

    public static final MenuItemType LINK     = new MenuItemType("LINK", "Link");
    public static final MenuItemType CATEGORY = new MenuItemType("CATEGORY", "ProductUsed");
    public static final MenuItemType PAGE     = new MenuItemType("PAGE", "Page");
    public static final MenuItemType SUBMENU  = new MenuItemType("SUBMENU", "Sub MenuService");
    public static final MenuItemType PRODUCT  = new MenuItemType("PRODUCT", "Device");
    public static final MenuItemType CUSTOM   = new MenuItemType("CUSTOM", "Custom");


    public static MenuItemType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public MenuItemType() {
        //do nothing
    }

    public MenuItemType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MenuItemType other = (MenuItemType) obj;
        if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
