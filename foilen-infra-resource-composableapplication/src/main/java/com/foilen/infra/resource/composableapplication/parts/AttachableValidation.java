/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronExpression;

import com.foilen.infra.plugin.v1.core.visual.helper.CommonFieldHelper;
import com.foilen.smalltools.tuple.Tuple2;
import com.google.common.base.Strings;

public class AttachableValidation {

    public static List<Tuple2<String, String>> validateCron(Map<String, String> formValues) {
        Set<String> fieldNames = formValues.keySet();
        return validateCron(formValues, fieldNames.toArray(new String[fieldNames.size()]));
    }

    public static List<Tuple2<String, String>> validateCron(Map<String, String> formValues, String... fieldNames) {
        List<Tuple2<String, String>> errors = new ArrayList<>();
        for (String fieldName : CommonFieldHelper.getAllFieldNames(formValues, fieldNames)) {
            String fieldValue = formValues.get(fieldName);
            errors.addAll(validateCron(fieldName, fieldValue));
        }
        return errors;
    }

    public static List<Tuple2<String, String>> validateCron(String fieldName, String fieldValue) {
        List<Tuple2<String, String>> errors = new ArrayList<>();
        if (!Strings.isNullOrEmpty(fieldValue)) {
            if (!CronExpression.isValidExpression(fieldValue)) {
                errors.add(new Tuple2<>(fieldName, "error.notCronTime"));
            }
        }
        return errors;
    }
}
