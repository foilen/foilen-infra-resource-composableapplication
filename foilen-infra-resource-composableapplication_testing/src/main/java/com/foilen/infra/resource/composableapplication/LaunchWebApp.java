/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.service.IPResourceService;
import com.foilen.infra.plugin.v1.core.service.internal.InternalChangeService;
import com.foilen.infra.resource.composableapplication.parts.AttachableService;
import com.foilen.infra.resource.composableapplication.parts.AttachableServiceEditor;

public class LaunchWebApp extends com.foilen.infra.plugin.core.system.fake.LaunchWebApp {

    public static void main(String[] args) {
        com.foilen.infra.plugin.core.system.fake.LaunchWebApp.main(args, LaunchWebApp.class);
    }

    @Override
    protected void createFakeData(IPResourceService resourceService, InternalChangeService internalChangeService) {
        super.createFakeData(resourceService, internalChangeService);

        // Fake data
        ChangesContext changes = new ChangesContext(resourceService);
        AttachableService attachableService = new AttachableService("java-app", "java -jar myapp.jar", null);
        attachableService.setResourceEditorName(AttachableServiceEditor.EDITOR_NAME);
        changes.resourceAdd(attachableService);
        internalChangeService.changesExecute(changes);
    }

}
