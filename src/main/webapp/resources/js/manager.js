// Darrel-Day Guerrero

$(document).ready(function() {

	// Toggle Icon For Nav Bar
	$('[data-toggle=collapse]').click(function(){
	  	$(this).find("i").toggleClass("glyphicon-chevron-right glyphicon-chevron-down");
	});
	
	var orderSearchFormType = $("[id='orderSearchForm:type']");
	var searchFormTypeClient = $("[id='clientSearchForm:type']");
	var searchFormType = $("[id='bookSearchForm:type']");
		
	searchFormTypeClient.attr('value', 'Id');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormTypeClient.attr('value', $(this).text());
	});
	
	searchFormType.attr('value', 'Title');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormType.attr('value', $(this).text());
	});
	
	orderSearchFormType.attr('value', 'Sale ID');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		orderSearchFormType.attr('value', $(this).text());
	});

	
	var phone = $("[id='userForm:phonenumber']");
	var cell = $("[id='userForm:cellnumber']");
	var postal = $("[id='userForm:postalcode']");
	
	var phone2 = $("[id='userFormEdit:phonenumber']");
	var cell2 = $("[id='userFormEdit:cellnumber']");
	var postal2 = $("[id='userFormEdit:postalcode']");
	
	
	
	phone.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});
	
	cell.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});
	
	postal.formatter({
		'pattern': '{{a}}{{9}}{{a}} {{9}}{{a}}{{9}}',
		'persistent': false
	});
	
	phone2.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});
	
	cell2.formatter({
		'pattern': '({{999}}) {{999}} {{9999}}',
		'persistent': false
	});
	
	postal2.formatter({
		'pattern': '{{a}}{{9}}{{a}} {{9}}{{a}}{{9}}',
		'persistent': false
	});




});