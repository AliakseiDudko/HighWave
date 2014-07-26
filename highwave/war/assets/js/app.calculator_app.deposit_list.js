App.CalculatorApp.DepositList = function() {
    var DepositList = {};

    var DepositDetailView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("deposit-detail-template")
    });

    var AccountStatementItemView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Backbone.Marionette.TemplateCache.get("account-statement-list-item-template")
    });

    var AccountStatementView = Marionette.CompositeView.extend({
        template: Backbone.Marionette.TemplateCache.get("account-statement-list-template"),
        className: "table table-striped",
        tagName: "table",
        childView: AccountStatementItemView
    });

    var DepositView = Marionette.ItemView.extend({
        tagName: "tr",
        template: Backbone.Marionette.TemplateCache.get("deposit-list-item-template"),

        events: {
            "click": "showDepositDetail"
        },

        showDepositDetail: function() {
            App.CalculatorApp.AccountStatement.reset(this.model.get("accountStatement"));
            
            var detailView = new DepositDetailView({
                model: this.model
            });
            App.modalRegion.show(detailView);

            var accountStatementView = new AccountStatementView({
                collection: App.CalculatorApp.AccountStatement
            });
            App.accountStatementRegion.show(accountStatementView);
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