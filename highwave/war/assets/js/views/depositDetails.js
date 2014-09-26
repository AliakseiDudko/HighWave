define([ "marionette", "models/accountStatement", "views/accountStatement", "text!templates/deposit-details-template.html" ], function(
        Marionette, AccountStatement, AccountStatementView, templateHtml) {
    var DepositDetailsView = Marionette.ItemView.extend({
        template: _.template(templateHtml),

        onShow: function() {
            var statementView = new AccountStatementView({
                model: this.model,
                collection: new AccountStatement(this.model.get("accountStatement"))
            });
            statementView.render();
            $("#account-statement-region").append(statementView.el);
        }
    });

    return DepositDetailsView;
});