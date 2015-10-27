package org.ei.opensrp.indonesia.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import org.ei.opensrp.domain.ANCServiceType;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.domain.ServiceProvided;
import org.ei.opensrp.indonesia.R;
import org.ei.opensrp.indonesia.view.contract.KartuIbuClient;
import org.ei.opensrp.indonesia.view.controller.KartuIbuRegisterController;
import org.ei.opensrp.indonesia.view.viewHolder.NativeKIRegisterViewHolder;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.contract.AlertDTO;
import org.ei.opensrp.view.contract.ServiceProvidedDTO;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.ei.opensrp.view.viewHolder.ECProfilePhotoLoader;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;
import org.ei.opensrp.view.viewHolder.ProfilePhotoLoader;

import android.graphics.Color;

import com.google.common.base.Strings;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.ei.opensrp.view.controller.ECSmartRegisterController.ANC_STATUS;
import static org.ei.opensrp.view.controller.ECSmartRegisterController.PNC_STATUS;
import static org.ei.opensrp.view.controller.ECSmartRegisterController.STATUS_TYPE_FIELD;

/**
 * Created by Dimas Ciputra on 2/16/15.
 */
public class KIClientsProvider implements SmartRegisterClientsProvider {

    private final LayoutInflater inflater;
    private final Context context;
    private final View.OnClickListener onClickListener;
    private final ProfilePhotoLoader photoLoader;

    private final AbsListView.LayoutParams clientViewLayoutParams;

    private final String maleChildAgeFormatString;
    private final String femaleChildAgeFormatString;

    private Drawable iconPencilDrawable;

    protected KartuIbuRegisterController controller;

    public KIClientsProvider(Context context, View.OnClickListener onClickListener, KartuIbuRegisterController controller) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.controller = controller;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        photoLoader = new ECProfilePhotoLoader(context.getResources(),
                context.getResources().getDrawable(R.drawable.woman_placeholder));

        clientViewLayoutParams = new AbsListView.LayoutParams(MATCH_PARENT,
                (int) context.getResources().getDimension(R.dimen.list_item_height));


        maleChildAgeFormatString = context.getResources().getString(R.string.ec_register_male_child);
        femaleChildAgeFormatString = context.getResources().getString(R.string.ec_register_female_child);

    }

    @Override
    public View getView(SmartRegisterClient client, View convertView, ViewGroup viewGroup) {
        ViewGroup itemView;
        NativeKIRegisterViewHolder viewHolder;
        if (convertView == null) {
            itemView = (ViewGroup) inflater().inflate(R.layout.smart_register_ki_client, null);
            viewHolder = new NativeKIRegisterViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            itemView = (ViewGroup) convertView;
            viewHolder = (NativeKIRegisterViewHolder) itemView.getTag();
        }

        KartuIbuClient kartuIbuClient = (KartuIbuClient) client;
        setupClientProfileView(kartuIbuClient, viewHolder);
        setupChildrenView(kartuIbuClient, viewHolder);
        setupEditView(kartuIbuClient, viewHolder);
        setupClientNoIbuView(kartuIbuClient, viewHolder);
        setupObsetriView(kartuIbuClient, viewHolder);
        setupClientEDDView(kartuIbuClient, viewHolder);
        setupStatusView(kartuIbuClient, viewHolder);

        itemView.setLayoutParams(clientViewLayoutParams);
        return itemView;
    }

    private void setupClientProfileView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.profileInfoLayout().bindData(client, photoLoader);
        viewHolder.profileInfoLayout().setOnClickListener(onClickListener);
        viewHolder.profileInfoLayout().setTag(client);
    }

    private void setupChildrenView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.childrenView().bindData(client, maleChildAgeFormatString, femaleChildAgeFormatString);
    }

    private void setupClientNoIbuView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.txtNoIbu().setText(String.valueOf(client.getNoIbu()));
    }

    private void setupObsetriView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.bidanObsetriView().bind(client);
    }

    private void setupClientEDDView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.txtEdd().setText(String.valueOf(client.getEdd()));
        viewHolder.txtEddDue().setText(String.valueOf(client.getDueEdd()));
    }

    private void setupEditView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        if (iconPencilDrawable == null) {
            iconPencilDrawable = context.getResources().getDrawable(R.drawable.ic_pencil);
        }
        viewHolder.editButton().setImageDrawable(iconPencilDrawable);
        viewHolder.editButton().setOnClickListener(onClickListener);
        viewHolder.editButton().setTag(client);
    }

    private void setupStatusView(KartuIbuClient client, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.statusView().bindData(client);

        // get status of mother
        String motherStatus = client.status().get(STATUS_TYPE_FIELD);

        if (Strings.isNullOrEmpty(motherStatus)) {
            return;
        }

        if(motherStatus.equalsIgnoreCase(ANC_STATUS)) {
            AlertDTO anc1VisitAlert = client.getANCAlertByCategory(KartuIbuClient.CATEGORY_ANC);
            if(anc1VisitAlert != null && !Strings.isNullOrEmpty(anc1VisitAlert.name())) {
                ViewGroup statusViewGroup = viewHolder.statusView().statusLayout(motherStatus);
                setStatusView(statusViewGroup, anc1VisitAlert, viewHolder);
            }
        } else if (motherStatus.equalsIgnoreCase(PNC_STATUS)) {
            AlertDTO pncVisitAlert = client.getANCAlertByCategory(KartuIbuClient.CATEGORY_PNC);
            if(pncVisitAlert != null && !Strings.isNullOrEmpty(pncVisitAlert.name())) {
                ViewGroup statusViewGroup = viewHolder.statusView().statusLayout(motherStatus);
                setStatusView(statusViewGroup, pncVisitAlert, viewHolder);
            }
        } else if (motherStatus.equalsIgnoreCase(KartuIbuClient.CATEGORY_KB)) {
            AlertDTO kbAlert = client.getANCAlertByCategory(KartuIbuClient.CATEGORY_KB);
            if(!Strings.isNullOrEmpty(kbAlert.name())) {
                ViewGroup statusViewGroup = viewHolder.statusView().statusLayout(motherStatus);
                setStatusView(statusViewGroup, kbAlert, viewHolder);
            }
        }
    }

    private void setStatusView(ViewGroup statusViewGroup, AlertDTO alert, NativeKIRegisterViewHolder viewHolder) {
        viewHolder.statusView().typeView(statusViewGroup).setText(alert.name());
        viewHolder.statusView().dateView(statusViewGroup).setText(alert.date());
        viewHolder.statusView().statusView(statusViewGroup).setText(alert.status().toUpperCase());
        viewHolder.statusView().setBackgroundColor(alert.alertStatus().backgroundColorResourceId());
        viewHolder.statusView().typeView(statusViewGroup).setTextColor(alert.alertStatus().fontColor());
        viewHolder.statusView().dateView(statusViewGroup).setTextColor(alert.alertStatus().fontColor());
        viewHolder.statusView().statusView(statusViewGroup).setTextColor(alert.alertStatus().fontColor());
        viewHolder.statusView().labelDateView(statusViewGroup).setText(R.string.str_due);
        viewHolder.statusView().labelDateView(statusViewGroup).setTextColor(alert.alertStatus().fontColor());
    }

    @Override
    public SmartRegisterClients getClients() {
        return controller.getKartuIbuClients();
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption, FilterOption searchFilter, SortOption sortOption) {
        return getClients().applyFilter(villageFilter, serviceModeOption, searchFilter, sortOption);
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {

    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    public LayoutInflater inflater() {
        return inflater;
    }
}