$(document).ready(function() {

	var epostalCode = $("[id='updateBillingInfoForm:epostalcode']");
	var spostalCode = $("[id='updateShippingInfoForm:spostalcode']");
	var ephoneNumber = $("[id='updatePersonalInfoForm:ephonenumber']");
	var ecellNumber = $("[id='updatePersonalInfoForm:ecellnumber']");
	
	
	
	//add formatters to fields below
	ephoneNumber.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});

	ecellNumber.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});

	epostalCode.formatter({
		'pattern': '{{a}}{{9}}{{a}} {{9}}{{a}}{{9}}',
		'persistent': false
	});

	spostalCode.formatter({
		'pattern': '{{a}}{{9}}{{a}} {{9}}{{a}}{{9}}',
		'persistent': false
	});
	
	$('.btn.btn-bookoo').click(function() {
		this.blur();
		$('.alert-message').fadeTo(0, 500).slideDown(500, function() {
			  $(this).show(); 
	    });
		window.setTimeout(function() {
		    $(".alert-message").fadeTo(500, 0).slideUp(500, function(){
		        $(this).hide(); 
		    }); }, 3000);
	
	});
	
});