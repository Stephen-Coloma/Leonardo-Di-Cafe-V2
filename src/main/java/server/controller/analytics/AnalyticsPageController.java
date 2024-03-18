package server.controller.analytics;

import server.model.analytics.AnalyticsPageModel;
import server.view.analytics.AnalyticsPageView;
import util.PushNotification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnalyticsPageController {
    private final AnalyticsPageModel model;
    private final AnalyticsPageView view;

    public AnalyticsPageController(AnalyticsPageModel model, AnalyticsPageView view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        setDate();

        addDateListeners();

        model.computeSales(null, null);
        view.setSalesLabel("₱" + model.getSales());
        view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));

        view.populateTableFromMap(model.getFoodList(), model.getBeverageList());
    } // end of start

    private void addDateListeners() {
        view.getStartDate().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDate endDate = view.getEndDate().getValue();
                if (endDate != null && newValue.isAfter(endDate)) {
                    PushNotification.toastWarn("Date Mismatch", "Start date should not be after the end date");
                    view.getStartDate().setValue(oldValue);
                } else {
                    model.computeSales(newValue, endDate);
                }
                view.setSalesLabel("₱" + model.getSales());
                view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));
            }
        });

        view.getEndDate().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDate startDate = view.getStartDate().getValue();
                if (startDate != null && newValue.isBefore(startDate)) {
                    PushNotification.toastWarn("Date Mismatch", "End date should not be before the start date");
                    view.getEndDate().setValue(oldValue);
                } else {
                    if (startDate == null) {
                        PushNotification.toastWarn("Date Mismatch", "Start date should be defined first");
                        view.getEndDate().setValue(oldValue);
                        view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));
                    } else {
                        model.computeSales(startDate, newValue);
                        view.setSalesLabel("₱" + model.getSales());
                        view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));
                    }
                }
            }
        });

        view.getStartDate().getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                    LocalDate endDate = view.getEndDate().getValue();
                    view.getStartDate().setValue(null);
                    model.computeSales(view.getStartDate().getValue(), endDate);
                    view.setSalesLabel("₱" + model.getSales());
                    view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));
            }
        });

        view.getEndDate().getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                LocalDate startDate = view.getEndDate().getValue();
                view.getEndDate().setValue(null);
                model.computeSales(startDate, view.getEndDate().getValue());
                view.setSalesLabel("₱" + model.getSales());
                view.setTotalOrdersLabel(String.valueOf(model.getTotalOrders()));
            }
        });
    } // end of addDateListeners

    public void setDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        view.setDateLabel(formattedDate);
    } // end of setDate
} // end of AnalyticsPageController
