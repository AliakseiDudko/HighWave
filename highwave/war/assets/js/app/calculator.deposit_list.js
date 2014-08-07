App.CalculatorApp.DepositList = function() {
    var DepositList = {};

    var DepositView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Marionette.TemplateCache.get("deposit-list-item-template"),

        events: {
            "click": "showDepositDetails"
        },

        showDepositDetails: function() {
            App.CalculatorApp.DepositDetails.showDepositDetails(this.model);
        }
    });

    var DepositListEmptyView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Marionette.TemplateCache.get("deposit-list-emtpy-template")
    });

    var DepositListView = Marionette.CompositeView.extend({
        template: Marionette.TemplateCache.get("deposit-list-template"),
        className: "table table-striped table-hover",
        tagName: "table",
        childView: DepositView,
        emptyView: DepositListEmptyView,

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("search:error", function() {
                self.showMessage("Error, please retry later.");
            });
            App.vent.on("search:noSearchTerm", function() {
                self.showMessage("Please enter amount and period.");
            });
            App.vent.on("search:noResults", function() {
                self.showMessage("No resuls were found.");
            });
        }
    });

    DepositList.showDeposits = function(deposits) {
        var depositListView = new DepositListView({
            collection: deposits
        });
        App.resultsRegion.show(depositListView);
    };

    return DepositList;
}();