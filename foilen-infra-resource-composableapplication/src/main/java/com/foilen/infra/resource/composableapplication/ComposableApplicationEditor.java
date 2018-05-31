/*
    Foilen Infra Resource Composable Application
    https://github.com/foilen/foilen-infra-resource-composableapplication
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.composableapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.service.TranslationService;
import com.foilen.infra.plugin.v1.core.visual.PageDefinition;
import com.foilen.infra.plugin.v1.core.visual.editor.ResourceEditor;
import com.foilen.infra.plugin.v1.core.visual.helper.CommonFormatting;
import com.foilen.infra.plugin.v1.core.visual.helper.CommonPageItem;
import com.foilen.infra.plugin.v1.core.visual.helper.CommonResourceLink;
import com.foilen.infra.plugin.v1.core.visual.helper.CommonValidation;
import com.foilen.infra.plugin.v1.core.visual.pageItem.field.InputTextFieldPageItem;
import com.foilen.infra.plugin.v1.model.resource.LinkTypeConstants;
import com.foilen.infra.resource.machine.Machine;
import com.foilen.infra.resource.unixuser.UnixUser;
import com.foilen.smalltools.tuple.Tuple2;

public class ComposableApplicationEditor implements ResourceEditor<ComposableApplication> {

    public static final String EDITOR_NAME = "Composable Application";

    @Override
    public void fillResource(CommonServicesContext servicesCtx, ChangesContext changesContext, Map<String, String> validFormValues, ComposableApplication editedResource) {
        editedResource.setName(validFormValues.get(ComposableApplication.PROPERTY_NAME));

        CommonResourceLink.fillResourceLink(servicesCtx, editedResource, LinkTypeConstants.RUN_AS, UnixUser.class, "unixUser", validFormValues, changesContext);
        CommonResourceLink.fillResourcesLink(servicesCtx, editedResource, LinkTypeConstants.INSTALLED_ON, Machine.class, "machines", validFormValues, changesContext);
        CommonResourceLink.fillResourcesLink(servicesCtx, editedResource, ComposableApplication.LINK_TYPE_ATTACHED, AttachablePart.class, "attachableParts", validFormValues, changesContext);
    }

    @Override
    public void formatForm(CommonServicesContext servicesCtx, Map<String, String> rawFormValues) {
        CommonFormatting.toLowerCase(rawFormValues, ComposableApplication.PROPERTY_NAME);
        CommonFormatting.trimSpaces(rawFormValues, ComposableApplication.PROPERTY_NAME);
    }

    @Override
    public Class<ComposableApplication> getForResourceType() {
        return ComposableApplication.class;
    }

    @Override
    public PageDefinition providePageDefinition(CommonServicesContext servicesCtx, ComposableApplication editedResource) {

        TranslationService translationService = servicesCtx.getTranslationService();
        PageDefinition pageDefinition = new PageDefinition(translationService.translate("ComposableApplicationEditor.title"));

        InputTextFieldPageItem nameField = CommonPageItem.createInputTextField(servicesCtx, pageDefinition, "ComposableApplicationEditor.name", ComposableApplication.PROPERTY_NAME);

        CommonResourceLink.addResourcePageItem(servicesCtx, pageDefinition, editedResource, LinkTypeConstants.RUN_AS, UnixUser.class, "ComposableApplicationEditor.unixUser", "unixUser");
        CommonResourceLink.addResourcesPageItem(servicesCtx, pageDefinition, editedResource, LinkTypeConstants.INSTALLED_ON, Machine.class, "ComposableApplicationEditor.machines", "machines");
        CommonResourceLink.addResourcesPageItem(servicesCtx, pageDefinition, editedResource, ComposableApplication.LINK_TYPE_ATTACHED, AttachablePart.class,
                "ComposableApplicationEditor.attachableParts", "attachableParts");

        if (editedResource != null) {
            nameField.setFieldValue(editedResource.getName());
        }

        return pageDefinition;
    }

    @Override
    public List<Tuple2<String, String>> validateForm(CommonServicesContext servicesCtx, Map<String, String> rawFormValues) {
        List<Tuple2<String, String>> errors = new ArrayList<>();
        errors.addAll(CommonValidation.validateNotNullOrEmpty(rawFormValues, ComposableApplication.PROPERTY_NAME));
        errors.addAll(CommonValidation.validateAlphaNumLower(rawFormValues, ComposableApplication.PROPERTY_NAME));
        return errors;
    }

}
