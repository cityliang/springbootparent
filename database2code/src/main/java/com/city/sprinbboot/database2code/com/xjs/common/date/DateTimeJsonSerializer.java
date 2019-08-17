package com.city.sprinbboot.database2code.com.xjs.common.date;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * ClassName:DateJsonSerializer <br/>
 * Function: 日期类型格式化，格式化为：yyyy-MM-dd HH:mm:ss格式. 用法如下：<br/>
 * Reason:   @JsonSerialize(using=DateTimeJsonSerializer.class)
 *
 * @Column(name="BIRTHDAY") public Date getBirthday() {
 * return birthday;
 * }
 * . <br/>
 */
public class DateTimeJsonSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }

}