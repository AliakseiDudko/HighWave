App.CalculatorApp.DepositList = function() {
    var DepositList = {};

    var DepositDetailView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("deposit-detail-template")
    });

    var DepositView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Backbone.Marionette.TemplateCache.get("deposit-template"),

        events: {
            "click": "showDepositDetail"
        },

        showDepositDetail: function() {
            var detailView = new DepositDetailView({
                model: this.model
            });
            App.modalRegion.show(detailView);
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