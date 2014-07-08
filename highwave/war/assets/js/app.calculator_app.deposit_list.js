App.CalculatorApp.DepositList = function() {
	var DepositList = {};

	var DepositDetailView = Marionette.ItemView.extend({
		template : "#deposit-detail-template"
	});

	var DepositView = Marionette.ItemView.extend({
	    tagName : "tr",
	    template : "#deposit-template",

	    events : {
		    "click" : "showDepositDetail"
	    },

	    showDepositDetail : function() {
		    var detailView = new DepositDetailView({
			    model : this.model
		    });
		    App.modalRegion.show(detailView);
	    }
	});

	var DepositListView = Marionette.CompositeView.extend({
	    template : "#deposit-list-template",
	    className : "table table-striped",
	    tagName : "table",
	    childView : DepositView,

	    showMessage : function(message) {
		    console.log(message);
	    },

	    initialize : function() {
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

	var SearchView = Marionette.ItemView.extend({
	    template : "#search-template",

	    initialize : function() {
		    var self = this;
		    App.vent.on("search:start", function() {
		    });
		    App.vent.on("search:stop", function() {
		    });
		    App.vent.on("search:term", function(term) {
			    self.$("#amountText").val(term);
		    });
	    },

	    events : {
		    "click #searchButton" : "search"
	    },

	    search : function() {
		    var searchTerm = this.$("#amountText").val().trim();
		    if (searchTerm.length > 0) {
			    App.vent.trigger("search:term", searchTerm);
		    } else {
			    App.vent.trigger("search:noSearchTerm", searchTerm);
		    }
	    }
	});

	DepositList.showDeposits = function(deposits) {
		var depositListView = new DepositListView({
			collection : deposits
		});
		App.CalculatorApp.layout.results.show(depositListView);
	};

	App.vent.on("layout:rendered", function() {
		var searchView = new SearchView();
		App.CalculatorApp.layout.search.show(searchView);
	});

	return DepositList;
}();

App.vent.on("layout:rendered", function() {
	App.CalculatorApp.DepositList.showDeposits(App.CalculatorApp.Deposits);
});
