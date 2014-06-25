MyApp = new Backbone.Marionette.Application();

MyApp.addRegions({
	generatorRegion : "#generator",
	resultsRegion : "#results"
});

Bank = Backbone.Model.extend({});

Deposit = Backbone.Model.extend({});

Banks = Backbone.Collection.extend({
	model : Bank
});

Deposits = Backbone.Collection.extend({
	model : Deposit
});

BankView = Backbone.Marionette.ItemView.extend({
	template : "#bank-template",
	tagName : "tr",
	className : "bank"
});

DepositView = Backbone.Marionette.ItemView.extend({
	template : "#deposit-template",
	tagName : "tr",
	className : "deposit"
});

BanksView = Backbone.Marionette.CompositeView.extend({
	tagName : "table",
	id : "banks",
	template : "#banks-template",
	childView : BankView,

	attachHtml : function(collectionView, itemView) {
		collectionView.$("tbody").append(itemView.el);
	}
});

DepositsView = Backbone.Marionette.CompositeView.extend({
	tagName : "table",
	id : "deposits",
	template : "#deposits-template",
	childView : DepositView,

	attachHtml : function(collectionView, itemView) {
		collectionView.$("tbody").append(itemView.el);
	}
});

MyApp.addInitializer(function(options) {
	var banksView = new BanksView({
		collection : options.banks
	});
	var depositsView = new DepositsView({
		collection : options.deposits
	});

	MyApp.generatorRegion.show(banksView);
	MyApp.resultsRegion.show(depositsView);
});

$(document).ready(function() {
	var banks = new Banks([ new Bank({
		name : "BelarusBank"
	}) ]);

	var deposits = new Deposits([ new Deposit({
		name : "Wave",
		interest : "45"
	}), new Deposit({
		name : "7",
		interest : "45+"
	}) ]);

	MyApp.start({
		banks : banks,
		deposits : deposits
	});
});