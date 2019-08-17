package com.city.sprinbboot.database2code.com.xjs.common.web;

import java.io.Serializable;
import java.util.Map;

public class SearchParamsAnalysis<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    Class<T> clazz;

    public SearchParamsAnalysis(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Map<String, Object> getSearchParamsAnalysis(Map<String, Object> filters) {
        if (filters.size() > 0) {
            SearchFilter searchFilter;
            StringBuffer sqlCondition = new StringBuffer();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                searchFilter = (SearchFilter) entry.getValue();
                sqlCondition.append(" and ");
                sqlCondition.append(searchFilter.fieldName);
                sqlCondition.append(" ");
                switch (searchFilter.operator.ordinal()) {
                    case 0:
                        sqlCondition.append("='");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 1:
                        sqlCondition.append("like '%");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("%'");
                        break;
                    case 2:
                        sqlCondition.append(">'");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 3:
                        sqlCondition.append("<'");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 4:
                        sqlCondition.append(">='");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 5:
                        sqlCondition.append("<='");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 6:
                        sqlCondition.append(" is null'");
                        break;
                    case 7:
                        sqlCondition.append(" is not null'");
                        break;
                    case 8:

                        break;
                    case 9:
                        sqlCondition.append("!='");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;
                    case 10:
                        sqlCondition.append(" in (");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append(")");
                        break;
                    case 11:
                        sqlCondition.append("like '");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("%'");
                        break;
                    case 12:
                        sqlCondition.append("like '%");
                        sqlCondition.append(searchFilter.value);
                        sqlCondition.append("'");
                        break;

                }
            }
        }
        return null;
    }

}
