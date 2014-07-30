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
        template: Backbone.Marionette.TemplateCache.get("deposit-details-template"),

        onShow: function() {
            var statementView = new AccountStatementView({
                collection: new AccountStatement(this.model.get("accountStatement"))
            });
            statementView.render();
            $("#account-statement-region").append(statementView.el);
        }
    });

    DepositDetails.showDepositDetails = function(model) {
        var detailView = new DepositDetailView({
            model: model
        });
        App.modalRegion.show(detailView);
    };

    return DepositDetails;
}();