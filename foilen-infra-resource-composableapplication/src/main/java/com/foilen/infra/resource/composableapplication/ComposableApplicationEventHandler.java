/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import java.util.List;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.eventhandler.AbstractCommonMethodUpdateEventHandler;
import com.foilen.infra.plugin.v1.core.eventhandler.CommonMethodUpdateEventHandlerContext;
import com.foilen.infra.plugin.v1.core.exception.IllegalUpdateException;
import com.foilen.infra.plugin.v1.core.service.IPResourceService;
import com.foilen.infra.plugin.v1.core.visual.helper.CommonResourceLink;
import com.foilen.infra.plugin.v1.model.base.IPApplicationDefinition;
import com.foilen.infra.plugin.v1.model.resource.LinkTypeConstants;
import com.foilen.infra.resource.application.Application;
import com.foilen.infra.resource.machine.Machine;
import com.foilen.infra.resource.unixuser.UnixUser;
import com.foilen.infra.resource.website.Website;

public class ComposableApplicationEventHandler extends AbstractCommonMethodUpdateEventHandler<ComposableApplication> {

    @Override
    protected void commonHandlerExecute(CommonServicesContext services, ChangesContext changes, CommonMethodUpdateEventHandlerContext<ComposableApplication> context) {

        context.setManagedResourcesUpdateContentIfExists(true);

        context.addManagedResourceTypes(Application.class);

        IPResourceService resourceService = services.getResourceService();

        ComposableApplication composableApplication = context.getResource();

        // Get the links
        List<Machine> machines = resourceService.linkFindAllByFromResourceAndLinkTypeAndToResourceClass(composableApplication, LinkTypeConstants.INSTALLED_ON, Machine.class);
        List<UnixUser> unixUsers = resourceService.linkFindAllByFromResourceAndLinkTypeAndToResourceClass(composableApplication, LinkTypeConstants.RUN_AS, UnixUser.class);
        List<AttachablePart> attachableParts = resourceService.linkFindAllByFromResourceAndLinkTypeAndToResourceClass(composableApplication, ComposableApplication.LINK_TYPE_ATTACHED,
                AttachablePart.class);

        // Validate links
        boolean proceed = true;
        if (machines.isEmpty()) {
            logger.info("No machine to install on. Skipping");
            proceed = false;
        }
        if (unixUsers.size() > 1) {
            logger.warn("Too many unix user to run as");
            throw new IllegalUpdateException("Must have a singe unix user to run as. Got " + unixUsers.size());
        }

        Integer unixUserId = null;
        UnixUser unixUser = null;
        if (!unixUsers.isEmpty()) {
            unixUser = unixUsers.get(0);
            unixUserId = unixUser.getId();
        }

        if (proceed) {

            logger.debug("attachableParts ; amount {}", attachableParts.size());

            // Create an Application
            Application application = new Application();
            context.addManagedResources(application);
            application.setName(composableApplication.getName());
            application.setDescription(composableApplication.getResourceDescription());

            IPApplicationDefinition applicationDefinition = new IPApplicationDefinition();
            application.setApplicationDefinition(applicationDefinition);
            applicationDefinition.setFrom("ubuntu:16.04");
            applicationDefinition.setRunAs(unixUserId);

            // Attach parts in a deterministic order
            attachableParts.stream() //
                    .sorted((a, b) -> a.getResourceName().compareTo(b.getResourceName())) //
                    .forEach(attachablePart -> {
                        logger.debug("Attaching {} with type {}", attachablePart.getResourceName(), attachablePart.getClass().getName());
                        attachablePart.attachTo(services, changes, context, application, applicationDefinition);
                    });

            // Link machines
            CommonResourceLink.syncToLinks(services, changes, application, LinkTypeConstants.INSTALLED_ON, Machine.class, machines);

            // Link unix user
            CommonResourceLink.syncToLinks(services, changes, application, LinkTypeConstants.RUN_AS, UnixUser.class, unixUsers);

            // Sync link websites
            List<Website> websitesFrom = resourceService.linkFindAllByFromResourceClassAndLinkTypeAndToResource(Website.class, LinkTypeConstants.POINTS_TO, composableApplication);
            CommonResourceLink.syncFromLinks(services, changes, Website.class, LinkTypeConstants.POINTS_TO, application, websitesFrom);

        }

    }

    @Override
    public Class<ComposableApplication> supportedClass() {
        return ComposableApplication.class;
    }

}
