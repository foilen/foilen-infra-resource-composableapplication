/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.eventhandler.CommonMethodUpdateEventHandlerContext;
import com.foilen.infra.plugin.v1.model.base.IPApplicationDefinition;
import com.foilen.infra.plugin.v1.model.resource.AbstractIPResource;
import com.foilen.infra.resource.application.Application;

/**
 * Extend this class to provide attachable parts to a {@link ComposableApplication}.
 */
public abstract class AttachablePart extends AbstractIPResource {

    /**
     * What to do to attach this part to the application.
     *
     * @param services
     *            the services
     * @param changes
     *            the changes context
     * @param context
     *            the update context
     * @param application
     *            the {@link Application} that is composed
     * @param applicationDefinition
     *            the application definition of the {@link Application}
     */
    public abstract void attachTo(CommonServicesContext services, ChangesContext changes, CommonMethodUpdateEventHandlerContext<?> context, Application application,
            IPApplicationDefinition applicationDefinition);

}
