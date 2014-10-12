define([ "marionette", "models/accountStatement", "views/accountStatement", "text!templates/deposit-details-template-byr.html",
        "text!templates/deposit-details-template-cur.html" ], function(Marionette, AccountStatement, AccountStatementView, templateByrHtml,
        templateCurHtml) {
    var DepositDetailsView = Marionette.ItemView.extend({
        getTemplate: function() {
            return this.model.get("deposit").currency == "BYR" ? _.template(templateByrHtml) : _.template(templateCurHtml)
        },

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