/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication.parts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.foilen.smalltools.tuple.Tuple2;

public class AttachableValidationTest {

    private String fieldName = "test";

    private void assertCronTime(List<String> values, List<Boolean> isValids) {
        Map<String, String> map = new HashMap<>();
        fillValues(values, map);
        List<Tuple2<String, String>> errors = AttachableValidation.validateCron(map, fieldName);
        assertValues(errors, isValids);
    }

    private void assertCronTime(String value, boolean isValid) {
        Map<String, String> map = new HashMap<>();
        map.put(fieldName, value);
        List<Tuple2<String, String>> errors = AttachableValidation.validateCron(map, fieldName);
        Assert.assertEquals(isValid, errors.isEmpty());
    }

    private void assertValues(List<Tuple2<String, String>> errors, List<Boolean> isValids) {
        for (int i = 0; i < isValids.size(); ++i) {
            String positionFieldName = fieldName + "[" + i + "]";
            boolean hasError = errors.stream().filter(it -> it.getA().equals(positionFieldName)).findAny().isPresent();
            Assert.assertEquals("Field " + positionFieldName + " is not as expected", isValids.get(i), !hasError);
        }
    }

    private void fillValues(List<String> values, Map<String, String> map) {
        for (int i = 0; i < values.size(); ++i) {
            map.put(fieldName + "[" + i + "]", values.get(i));
        }
    }

    @Test
    public void testValidateCron() {
        assertCronTime("* * * * * ?", true);

        assertCronTime("0 0 * * * ?", true);
        assertCronTime("0 59 * * * ?", true);
        assertCronTime("0 */15 * * * ?", true);

        assertCronTime("0 * 0 * * ?", true);
        assertCronTime("0 * 23 * * ?", true);
        assertCronTime("0 * */15 * * ?", true);

        assertCronTime("abc", false);
        assertCronTime("0 60 * * * ?", false);
        assertCronTime("0 * 24 * * ?", false);
        assertCronTime("0 * * 0 * ?", false);
        assertCronTime("0 * * 32 * ?", false);
        assertCronTime("0 * * * 0 ?", false);
        assertCronTime("0 * * * 13 ?", false);
        assertCronTime("0 * * * * 7", false);
        assertCronTime("0 * * *", false);

        assertCronTime("1,11,21,31,41,51 * * * * ?", true);
        assertCronTime("1,11,21,31,41,51,61 * * * * ?", false);

        assertCronTime(Arrays.asList("* * * * * ?", "1,11,21,31,41,71 * * * * ?"), Arrays.asList(true, false));
    }
}
