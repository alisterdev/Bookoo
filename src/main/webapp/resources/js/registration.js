$(document).ready(function() {

	//save references to fields and add HTML5 placeholders
	
	var firstName = $("[id='registrationForm:firstname']");
	firstName.attr('placeholder', 'John');
	
	var lastName = $("[id='registrationForm:lastname']");
	lastName.attr('placeholder', 'Doe');
	
	var company = $("[id='registrationForm:company']");
	company.attr('placeholder', 'Bookoo Inc');
	
	var email = $("[id='registrationForm:email']");
	email.attr('placeholder', 'johndoe@gmail.com');

	var postalCode = $(".postalcode");
	postalCode.attr('placeholder', 'H1H 1H1');
		
	var phoneNumber = $("[id='registrationForm:phonenumber']");
	phoneNumber.attr('placeholder', '999 999 9999');
	
	var cellNumber = $("[id='registrationForm:cellnumber']");
	cellNumber.attr('placeholder', '999 999 9999');
	
	//add formatters to fields below
	phoneNumber.formatter({
		'pattern': '({{999}}) {{999}}-{{9999}}',
		'persistent': false
	});

	cellNumber.formatter({
		'pattern': '({{999}}) {{999}}-{{9999}}',
		'persistent': false
	});

	postalCode.formatter({
		'pattern': '{{a}}{{9}}{{a}} {{9}}{{a}}{{9}}',
		'persistent': false
	});
	
	
});