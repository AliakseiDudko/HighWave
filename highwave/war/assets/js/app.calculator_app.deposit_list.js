App.CalculatorApp.DepositList = function() {
    var DepositList = {};

    var DepositView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Backbone.Marionette.TemplateCache.get("deposit-list-item-template"),

        events: {
            "click": "showDepositDetails"
        },

        showDepositDetails: function() {
            App.CalculatorApp.DepositDetails.showDepositDetails(this.model);
        }
    });

    var DepositListEmptyView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Backbone.Marionette.TemplateCache.get("deposit-list-emtpy-template")
    });

    var DepositListView = Marionette.CompositeView.extend({
        template: Backbone.Marionette.TemplateCache.get("deposit-list-template"),
        className: "table table-striped",
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
                self.showMessage("Error, please retry later :s");
            });
            App.vent.on("search:noSearchTerm", function() {
                self.showMessage("Hummmm, can do better :)");
            });
            App.vent.on("search:noResults", function() {
                self.showMessage("No books found");
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