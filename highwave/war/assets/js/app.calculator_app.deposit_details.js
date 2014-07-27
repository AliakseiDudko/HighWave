App.CalculatorApp.DepositDetails = function() {
    var DepositDetails = {};

    var AccountStatementItem = Backbone.Model.extend();

    var AccountStatement = Backbone.Collection.extend({
        model: AccountStatementItem
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

    var DepositDetailView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("deposit-details-template")
    });

    DepositDetails.showDepositDetails = function(model) {
        var detailView = new DepositDetailView({
            model: model
        });
        App.modalRegion.show(detailView);

        var collection = new AccountStatement(model.get("accountStatement"));
        var accountStatementView = new AccountStatementView({
            collection: collection
        });
        App.accountStatementRegion.show(accountStatementView);
    };

    return DepositDetails;
}();