/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import com.foilen.infra.plugin.v1.model.resource.AbstractIPResource;
import com.foilen.infra.plugin.v1.model.resource.InfraPluginResourceCategory;
import com.foilen.infra.resource.application.Application;
import com.foilen.infra.resource.machine.Machine;
import com.foilen.infra.resource.unixuser.UnixUser;
import com.foilen.infra.resource.website.Website;

/**
 * This is an application that is composed of multiple parts. <br/>
 * Links to:
 * <ul>
 * <li>{@link UnixUser}: (optional / 1) RUN_AS - The default user that executes the services on this application</li>
 * <li>{@link Machine}: (optional / many) INSTALLED_ON - The machines where to install that server</li>
 * <li>{@link AttachablePart}: (optional / many) ATTACHED - The parts to attach</li>
 * </ul>
 *
 * Manages:
 * <ul>
 * <li>{@link Application}: The application with all the parts</li>
 * </ul>
 *
 * Links from:
 * <ul>
 * <li>{@link Website}: (optional / many) POINTS_TO - Websites that will point to the application</li>
 * </ul>
 */
public class ComposableApplication extends AbstractIPResource {

    public static final String PROPERTY_NAME = "name";
    public static final String LINK_TYPE_ATTACHED = "ATTACHED";

    // Details
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public InfraPluginResourceCategory getResourceCategory() {
        return InfraPluginResourceCategory.INFRASTRUCTURE;
    }

    @Override
    public String getResourceDescription() {
        return "Composable Application";
    }

    @Override
    public String getResourceName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
