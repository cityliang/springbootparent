package com.city.sprinbboot.database2code.core.common.entity;

import com.city.sprinbboot.database2code.core.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@SuppressWarnings("serial")
public abstract class AbstractEntity<ID> extends BaseEntity {

    public abstract ID getId();

    public abstract void setId(ID id);

    public boolean isNew() {
        return null == getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        @SuppressWarnings("unchecked")
        AbstractEntity<ID> that = (AbstractEntity<ID>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
