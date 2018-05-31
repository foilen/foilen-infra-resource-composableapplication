/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import java.util.Arrays;
import java.util.Collections;

import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.plugin.IPPluginDefinitionProvider;
import com.foilen.infra.plugin.v1.core.plugin.IPPluginDefinitionV1;
import com.foilen.infra.resource.composableapplication.parts.AttachableMariaDB;
import com.foilen.infra.resource.composableapplication.parts.AttachableMariaDBEditor;
import com.foilen.infra.resource.composableapplication.parts.AttachableService;
import com.foilen.infra.resource.composableapplication.parts.AttachableServiceEditor;

public class FoilenComposableApplicationPluginDefinitionProvider implements IPPluginDefinitionProvider {

    @Override
    public IPPluginDefinitionV1 getIPPluginDefinition() {
        IPPluginDefinitionV1 pluginDefinition = new IPPluginDefinitionV1("Foilen", "Application - Composable", "A way to create application by attaching parts", "1.0.0");

        pluginDefinition.addCustomResource(ComposableApplication.class, "Composable Application", //
                Arrays.asList(ComposableApplication.PROPERTY_NAME), //
                Collections.emptyList());
        pluginDefinition.addCustomResource(AttachableService.class, "Attachable Service", //
                Arrays.asList(AttachableService.PROPERTY_NAME), //
                Collections.emptyList());
        pluginDefinition.addCustomResource(AttachableMariaDB.class, "Attachable MariaDB", //
                Arrays.asList(AttachableMariaDB.PROPERTY_NAME), //
                Collections.emptyList());

        // Resource editors
        pluginDefinition.addTranslations("/com/foilen/infra/resource/composableapplication/messages");
        pluginDefinition.addResourceEditor(new ComposableApplicationEditor(), ComposableApplicationEditor.EDITOR_NAME);
        pluginDefinition.addResourceEditor(new AttachableServiceEditor(), AttachableServiceEditor.EDITOR_NAME);
        pluginDefinition.addResourceEditor(new AttachableMariaDBEditor(), AttachableMariaDBEditor.EDITOR_NAME);

        // Updater Handler
        pluginDefinition.addUpdateHandler(new ComposableApplicationEventHandler());

        return pluginDefinition;
    }

    @Override
    public void initialize(CommonServicesContext commonServicesContext) {
    }

}
