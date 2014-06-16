google_ad_client = "pub-3073868596887719";
google_ad_width = 180;
google_ad_height = 150;
google_ad_format = "180x150_as";
google_ad_type = "text";
google_ad_channel ="";
google_color_border = "E1F97B";
google_color_bg = "AFD118";
google_color_link = "FFFFFF";
google_color_text = "FFFFFF";
google_color_url = "647E00";

function ALstart() {	
	// Génération de l'image
	$('generator').onsubmit=function() {
		
		if($('error')) $('content').removeChild($('error'));

		/*if($('color1').value.length==3) {
			var tmp=$('color1').value.split('');
			$('color1').value=tmp[0]+tmp[0]+tmp[1]+tmp[1]+tmp[2]+tmp[2];
		}
		if($('color2').value.length==3) {
			var tmp=$('color2').value.split('');
			$('color2').value=tmp[0]+tmp[0]+tmp[1]+tmp[1]+tmp[2]+tmp[2];
		}*/

		if(($('color1').value.length!=6 || $('color2').value.length!=6)) {
			domEl('span','You must enter 2 hexadecimal numbers (ex: CC0000 and 00FF00)',[['id','error']],$('content'));
			return false;
		}
		
		ALindicator('visible');
		
		if($('trans').checked) var trans=1;
		else var trans=0;
		
		ALsendData('color1='+$('color1').value+'&color2='+$('color2').value+'&type='+$('type').value+'&trans='+trans);
		return false;
	}
	
	if($('top10')) {
		var lis=$('top10').getElementsByTagName('li');
		for(var i=0;i<lis.length;i++) {
			lis[i].onclick=function() {
				var img=this.style.backgroundImage.replace(')','');
				img=img.split('/');
				window.location='download.php?img='+img[img.length-1];
			}
		}
	}
	
	
	// Supprime les caractères invalides de la saisie
	$('color1').onkeyup=function(e) {
		var rech=new RegExp(/[^0-9A-Fa-f]/g);
		this.value=this.value.replace(rech,'');
	}

	$('color2').onkeyup=function(e) {
		var rech=new RegExp(/[^0-9A-Fa-f]/g);
		this.value=this.value.replace(rech,'');
	}
	
	// Génération du menu
	ALmakeMenu();
	// Génération du color-picker
	ALmakeColorPicker();
}


function ALsendData(data) {
    
    if($('trans').checked) var trans=1;
    else var trans=0;
    
    var c1 = $('color1').value.substring(0,2);
    var c2 = $('color1').value.substring(2,4);
    var c3 = $('color1').value.substring(4);
    
    var c4 = $('color2').value.substring(0,2);
    var c5 = $('color2').value.substring(2,4);
    var c6 = $('color2').value.substring(4);
    
    var url = 'cache/'+c1+'/'+c2+'/'+c3+'/'+c4+'/'+c5+'/'+c6+'/'+$('type').value+'-'+trans+'.gif';
    
    domEl('img','',[['src',url],['alt','activity indicator']],$('loader'),true);
    
    // Centre l'image
    $('loader').style.lineHeight=115+'px';
    $('loader').style.textAlign='center';
    
    
    if(!$('downloadit')) domEl('a','Download it',[['id','downloadit'],['href','download.php?img='+url]],$('previewinner'));
    else $('downloadit').setAttribute('href','download.php?img='+url);
    
    ALindicator('hidden');

    /*
    // Internet Explorer c'est mal
    if(window.ActiveXObject) var ALajax = new ActiveXObject("Microsoft.XMLHTTP") ;
    // les autres c'est bien
    else var ALajax = new XMLHttpRequest();

    ALajax.onreadystatechange = function() {
        if (ALajax.readyState == 4 && ALajax.status == 200) {
			
			if(!ALajax.responseXML) return false;
			
			var image=ALajax.responseXML.getElementsByTagName('image')[0];
			
			domEl('img','',[['src',image.getAttribute('url')],['alt','activity indicator']],$('loader'),true);

			// Centre l'image
			$('loader').style.lineHeight=115+'px';
			$('loader').style.textAlign='center';
			

			if(!$('downloadit')) domEl('a','Download it',[['id','downloadit'],['href','download.php?img='+image.getAttribute('url')]],$('previewinner'));
			else $('downloadit').setAttribute('href','download.php?img='+image.getAttribute('url'));
			
			ALindicator('hidden');
		}
    }    

	ALajax.open("POST", 'generate.php', true);
    ALajax.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=utf-8');
    ALajax.send(data);
    */
}

function ALindicator(view) {
	$('indicator').style.visibility=view;
}

function ALstartLite() {
	ALstart(true);
}

function xiti() {
	Xt_param = 's=278834&amp;p=Ajaxload';
	try {Xt_r = top.document.referrer;}
	catch(e) {Xt_r = document.referrer; }
	Xt_h = new Date();
	Xt_i = '<img ';
	Xt_i += 'src="http://logv32.xiti.com/hit.xiti?'+Xt_param;
	Xt_i += '&amp;hl='+Xt_h.getHours()+'x'+Xt_h.getMinutes()+'x'+Xt_h.getSeconds();
	if(parseFloat(navigator.appVersion)>=4)
	{Xt_s=screen;Xt_i+='&amp;r='+Xt_s.width+'x'+Xt_s.height+'x'+Xt_s.pixelDepth+'x'+Xt_s.colorDepth;}
	$('xiti-logo').innerHTML=Xt_i+'&amp;ref='+Xt_r.replace(/[<>"]/g, '').replace(/&/g, '$')+'" alt="Xiti" />';
}

function ALmakeMenu() {
	var types = $('type').getElementsByTagName('option');
	var typesL = types.length;
	
	var div = domEl('div','',[['id','typelist']]);
	
	for(var i=0;i<typesL;i++) {
		var img = domEl('img','',[['src','images/examples/'+types[i].value+'.gif'],['alt',types[i].text]]);
		var a = domEl('a',img,[['href','javascript:ALuseType('+types[i].value+')'],['title',types[i].text]],div);
	}	
	$('type').parentNode.insertBefore(div,$('type'));
	
	domEl('div','',[['id','layer']],$('type').parentNode);
	
	$('layer').onclick = function() {
		this.blur;
		$('typelist').style.display = 'block';
		document.onclick = function() {
			document.onclick = function() {
				$('type').style.display = '';
				$('typelist').style.display = '';
				document.onclick = null;
			}
		}
	}	
}

function ALmakeColorPicker() {
	myAddEventListener($('color1'),'focus',function() {
		$('color-picker').style.display = 'block';
		$('color-picker1').style.display = 'block';
		$('color-picker2').style.display = 'none';
	});
	
	myAddEventListener($('color2'),'focus',function() {
		$('color-picker').style.display = 'block';
		$('color-picker1').style.display = 'none';
		$('color-picker2').style.display = 'block';
	});
}

function ALuseType(id) {
	$('type').value = id;
	$('type').style.display = '';
	$('typelist').style.display = '';
}

if(window.addEventListener) window.addEventListener('load',ALstart,false);