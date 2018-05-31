/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.foilen.infra.plugin.core.system.fake.junits.AbstractIPPluginTest;
import com.foilen.infra.plugin.core.system.junits.JunitsHelper;
import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.service.IPResourceService;
import com.foilen.infra.plugin.v1.core.service.internal.InternalChangeService;
import com.foilen.infra.resource.composableapplication.ComposableApplication;
import com.foilen.infra.resource.composableapplication.ComposableApplicationEditor;
import com.foilen.infra.resource.composableapplication.parts.AttachableService;
import com.foilen.infra.resource.composableapplication.parts.AttachableServiceEditor;
import com.foilen.infra.resource.machine.Machine;
import com.foilen.infra.resource.unixuser.UnixUser;

public class ComposableApplicationEditorTest extends AbstractIPPluginTest {

    private AttachableService findAttachableServiceByName(String name) {
        IPResourceService resourceService = getCommonServicesContext().getResourceService();
        return resourceService.resourceFind(resourceService.createResourceQuery(AttachableService.class) //
                .propertyEquals(AttachableService.PROPERTY_NAME, name)) //
                .get();
    }

    private Machine findMachineByName(String name) {
        IPResourceService resourceService = getCommonServicesContext().getResourceService();
        return resourceService.resourceFind(resourceService.createResourceQuery(Machine.class) //
                .propertyEquals(Machine.PROPERTY_NAME, name)) //
                .get();
    }

    private UnixUser findUnixUserByName(String name) {
        IPResourceService resourceService = getCommonServicesContext().getResourceService();
        return resourceService.resourceFind(resourceService.createResourceQuery(UnixUser.class) //
                .propertyEquals(UnixUser.PROPERTY_NAME, name)) //
                .get();
    }

    @Test
    public void test() {

        // Create fake data
        IPResourceService resourceService = getCommonServicesContext().getResourceService();
        InternalChangeService internalChangeService = getInternalServicesContext().getInternalChangeService();

        ChangesContext changes = new ChangesContext(resourceService);
        changes.resourceAdd(new Machine("test1.node.example.com", "192.168.0.11"));
        changes.resourceAdd(new UnixUser(null, "user1", "/home/user1", null, null));
        AttachableService attachableService = new AttachableService("java-app", "java -jar myapp.jar", null);
        attachableService.setResourceEditorName(AttachableServiceEditor.EDITOR_NAME);
        changes.resourceAdd(attachableService);
        internalChangeService.changesExecute(changes);

        // Fill the form
        ComposableApplicationEditor resourceEditor = new ComposableApplicationEditor();

        Map<String, String> formValues = new HashMap<>();
        formValues.put(ComposableApplication.PROPERTY_NAME, "my_app");
        formValues.put("unixUser", String.valueOf(findUnixUserByName("user1").getInternalId()));
        formValues.put("machines", String.valueOf(findMachineByName("test1.node.example.com").getInternalId()));
        formValues.put("attachableParts", String.valueOf(findAttachableServiceByName("java-app").getInternalId()));

        assertEditorNoErrors(null, resourceEditor, formValues);

        // Assert
        JunitsHelper.assertState(getCommonServicesContext(), getInternalServicesContext(), "ComposableApplicationEditorTest-state.json", getClass(), true);
    }

}
