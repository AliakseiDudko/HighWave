App.CalculatorApp = function() {
	var CalculatorApp = {};

	var Layout = Marionette.LayoutView.extend({
	    template : "#calculator-layout",

	    regions : {
	        search : "#search-region",
	        results : "#results-region"
	    }
	});

	var Deposit = Backbone.Model.extend();

	var Deposits = Backbone.Collection.extend({
	    model : Deposit,

	    initialize : function() {
		    var self = this;

		    _.bindAll(this, "search");
		    App.vent.on("search:term", function(term) {
			    self.search(term);
		    })

		    this.maxResults = 40;
		    this.page = 0;
		    this.loading = false;
		    this.totalItems = null;
	    },

	    search : function(searchTerm) {
		    this.page = 0;

		    var self = this;
		    this.fetchDeposits(searchTerm, function(deposits) {
			    if (deposits.length > 0) {
				    self.reset(deposits);
			    } else {
				    App.vent.trigger("search:noResults");
			    }
		    });
	    },

	    fetchDeposits : function(searchTerm, callback) {
		    if (this.loading) {
			    return true;
		    }

		    this.loading = true;

		    var self = this;
		    App.vent.trigger("search:start");

		    var query = "";
		    $.ajax({
		        url : "/_ah/api/deposits/v0/deposits",
		        data : query,
		        success : function(res) {
			        App.vent.trigger("search:stop");
			        if (res.items.length == 0) {
				        callback([]);
				        return [];
			        }
			        if (res.items) {
				        self.page++;
				        self.totalItems = res.totalItems;
				        var searchResults = [];
				        _.each(res.items, function(item) {
					        searchResults[searchResults.length] = new Deposit({
					            depositId : item.id,
					            name : item.displayName,
					            currency : item.currency
					        });
				        });
				        callback(searchResults);
				        self.loading = false;
				        return searchResults;
			        } else if (res.error) {
				        App.vent.trigger("search:error");
				        self.loading = false;
			        }
		        }
		    });
	    }
	});

	CalculatorApp.Deposits = new Deposits();

	CalculatorApp.initializeLayout = function() {
		CalculatorApp.layout = new Layout();
		CalculatorApp.layout.on("show", function() {
			App.vent.trigger("layout:rendered");
		});

		App.calculatorRegion.show(App.CalculatorApp.layout);
	};

	return CalculatorApp;
}();

App.addInitializer(function() {
	App.CalculatorApp.initializeLayout();
	App.vent.trigger("search:term", "Day 7");
});