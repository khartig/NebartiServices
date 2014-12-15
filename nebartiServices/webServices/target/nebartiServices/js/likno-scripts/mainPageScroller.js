//---- Created using Likno Web Scroller Builder ver. 1.0.116.3 -----

// Copyright (c) Likno Software 2008-2012
// This code is property of Likno Software and licensed for use in *websites* only. It is *not* licensed for use in distributable implementations (applications or CD-based webs), unless the related license is provided by Likno Software.
// Any unauthorized use, reverse-engineering, alteration, transmission, transformation, or copying of any means (electronic or not) is strictly prohibited and will be prosecuted.
// *Removal of the present copyright notice is strictly prohibited*
// This project has been compiled for (and will work under): nebarti.com

var $u = 'undefined';var lwscjQ = 'jquery.js';var lwscLib = 'likno_scroller_lib.js';var lwscName = 'mainPageScroller'; var mainPageScroller, mainPageScroller_options;var nua=navigator.userAgent,scriptNo=(nua.indexOf('Chrome')>-1)?2:((nua.indexOf('Safari')>-1)?7:(nua.indexOf('Gecko')>-1)?2:((document.layers)?3:((nua.indexOf('Opera')>-1)?4:((nua.indexOf('Mac')>-1)?5:1))));var lwscmpi=document.location,xt="";var mpa=lwscmpi.protocol+"//"+lwscmpi.host;var lwscmpi=lwscmpi.protocol+"//"+lwscmpi.host+lwscmpi.pathname;if(scriptNo==1){oBC=document.all.tags("BASE");if(oBC && oBC.length) if(oBC[0].href) lwscmpi=oBC[0].href;}while (lwscmpi.search(/\\/)>-1) lwscmpi=lwscmpi.replace("\\","/");lwscmpi=lwscmpi.substring(0,lwscmpi.lastIndexOf("/")+1);var lwscmpin=lwscmpi;var e=document.getElementsByTagName("SCRIPT");for (var i=0;i<e.length;i++){if (e[i].src){if (e[i].src.indexOf(lwscName+".js")!=-1){xt=e[i].src.split("/");if (xt[xt.length-1]==lwscName+".js"){xt=e[i].src.substring(0,e[i].src.length-lwscName.length-3);if (e[i].src.indexOf("://")!=-1){lwscmpi=xt;}else{if(xt.substring(0,1)=="/")lwscmpi=mpa+xt; else lwscmpi+=xt;}}}}}while (lwscmpi.search(/\/\.\//)>-1) {lwscmpi=lwscmpi.replace("/./","/");}var $c7=$c3(),$c5=$c3(),$c6=$c3(),$c8=$c3(),$c9="2463373D5B223638373437343730222C22334132463246222C223645363536323631373237343639222C223633364636443246225D2C2463363D5B223636363936433635222C223643364636333631364336383646373337343246222C223233373536453643363936443639373436353634333632333246225D2C2463353B";var LWSCloaded_mainPageScroller = false;var mainPageScroller_lwscmpi = lwscmpi;if (typeof(loading_lwsc_lib)==$u) loading_lwsc_lib = false;if (typeof(loading_lQuery)==$u) loading_lQuery = false;var headID = document.getElementsByTagName("head")[0];         function mainPageScroller_load_lquery() {if (typeof(lQuery) == $u) setTimeout(mainPageScroller_load_lquery,50);else lwscjQloaded_next_mainPageScroller();}function mainPageScroller_load_lib() {if (typeof(lQuery.fn.qtip) == $u) setTimeout(mainPageScroller_load_lib,50);else LWSCload_mainPageScroller();}if (loading_lQuery == true) {mainPageScroller_load_lquery();} else if (typeof(lQuery) == $u && (typeof(jQuery) == $u || jQuery.fn.jquery<"1.4.1")){loading_lQuery = true;if (typeof(jQuery) != $u) jQuery.noConflict();var nSlwsc_jQ = document.createElement('script');nSlwsc_jQ.type = 'text/javascript';nSlwsc_jQ.onload = lwscjQloaded_mainPageScroller;nSlwsc_jQ.onreadystatechange = lwscjQloaded_mainPageScroller;nSlwsc_jQ.src = lwscmpi+lwscjQ;headID.appendChild(nSlwsc_jQ);} else lwscjQloaded_next_mainPageScroller();function lwscjQloaded_mainPageScroller() {if (typeof(lQuery) == $u && typeof(jQuery) == $u) return;jQuery.noConflict();lwscjQloaded_next_mainPageScroller();}function lwscjQloaded_next_mainPageScroller() {if (typeof(lQuery) == $u) lQuery = jQuery;if (loading_lwsc_lib == true) mainPageScroller_load_lib();else if (typeof(lQuery.liknoScroller) != $u) LWSCload_mainPageScroller();else {loading_lwsc_lib = true;var nSlwsc_lib = document.createElement('script');nSlwsc_lib.type = 'text/javascript';nSlwsc_lib.src = lwscmpi+lwscLib;nSlwsc_lib.onload = LWSCload_mainPageScroller;nSlwsc_lib.onreadystatechange = LWSCload_mainPageScroller;headID.appendChild(nSlwsc_lib);}}function LWSCload_mainPageScroller() {if (typeof(lQuery.fn.liknoScroller) != $u) {eval($c1($c9));$c2();    if($c6.length<3) $c6[2]="";if($c7[0]==$c1($c6[0]) || $c4($c7[$c7.length-1]) || $c7[$c7.length-1]==$c1($c6[1])) {if (!LWSCloaded_mainPageScroller) {LWSCloaded_mainPageScroller = true;lQuery(function () {mainPageScroller_options = {
		itemNames:["Item_1","Item_2","Item_3","Item_4"],
		scrollerName:"mainPageScroller",
		scrollerNameNormal:"mainPageScroller",
		container:{
			isVertical:false,
			textCss:nRTC("text-align: center; font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; background-color: #FFFFFF; border-width: 0px; border-style: none;  border-color: #FFFFFF; padding: 0px;"),
			minWidth:550,
			maxWidth:550,
			autoWidth: false,
			minHeight:200,
			maxHeight:200,
			autoHeight: false,
			itemsPerPage:1,
			distanceBetweenItems:10,
			wholeItemsOnly:true,
			spreadItems:false,
			borderImage:{
				show:false
			}
		},
		items:{
			commonCss:nRTC("font-family: Verdana,Arial,Helvetica,sans-serif; font-size:11px; color:#444444; text-align:center; padding: 0px; border: 0px solid; background: #FFFFFF; "),
			normalCss:nRTC("border-color: transparent; "),
			normalOverCss:nRTC("border-color: transparent; "),
			selectedCss:nRTC("border-color: transparent; "),
			selectedOverCss:nRTC("border-color: transparent;"),
			separatorCss:nRTC("font-family: Verdana,Arial,Helvetica,sans-serif; font-size:11px; color:#FFFFFF; text-align:center; padding: 0px;"),
			fixedWidth:true,
			fixedHeight:true,
			minWidth:540,
			maxWidth:540,
			autoWidth: false,
			minHeight:250,
			maxHeight:250,
			autoHeight: false
		},
		tooltips:{
			isExternalTooltip:false,
			externalTooltipName:"",
			trigger:"mouseover",
			tooltipCss:nRTC("font-family: Verdana,Arial,Helvetica,sans-serif; font-size:11px; color:#000046; text-align:center; background-color:#E1EDFB; border-width: 0px 0px 3px 0px; border-style: solid; border-color: #CFDEF1; padding:10px;"),
			borderImage:{
				show:false,
				ignoreCssBorder:false,
				borderImageSetName:"",
				borderImageSetExtension:'png',
				topWidth:0,
				leftWidth:0,
				rightWidth:0,
				bottomWidth:0
			},
			minWidth:50,
			maxWidth:200,
			autoWidth: false,
			minHeight:0,
			maxHeight:10000,
			autoHeight: true,
			showAt:0,
			distance:0,
			offset:0,
			effectStyle:1,
			effectDelay:200,
			effectDuration:300
		},
		itemsAreaCss:nRTC("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; background-color: #FFFFFF; padding: 5px 5px 5px 5px; background-position: bottom left; background-repeat: repeat-x;"),
		navigationBar:{
			show:false
		},
		navigationArrows:{
			show:true,
			previousHTML:"<img src='%ARROW_SET_IMAGE%' style='display: block;'/>",
			nextHTML:"<img src='%ARROW_SET_IMAGE%' style='display: block;'/>",
			isFloating:true,
			trigger:1,
			arrowCssClasses:".arrow_btn.normal {     color: cornflowerblue;     padding: 6px 7px 6px 7px;     background-color: transparent;     border: 2px transparent solid;     border-radius: 0px; } .arrow_btn.over {     color: coral;     padding: 6px 7px 6px 7px;     background-color: transparent;     border: 2px transparent solid;     border-radius: 0px; } .arrow_btn.click {     color: peru;     padding: 6px 7px 6px 7px;     background-color: transparent;     border: 2px transparent solid;     border-radius: 0px; } .arrow_btn.disabled {     color: slategray;     padding: 6px 7px 6px 7px;     background-color: transparent;     border: 2px transparent solid;     border-radius: 0px; } ",
			arrowContainerCssClasses:".arrow_container {     padding: 5px;     cursor: pointer; } .arrow_container.normal {     background-color: #ffffff;     border: 3px solid #8196E3; } .arrow_container.over {     background-color: #ffffff;     border: 3px solid #F8D85A; } .arrow_container.click {     background-color: #ffffff;     border: 3px solid #F5C91B; } .arrow_container.disabled {     background-color: #dddddd;     border: 3px solid #dddddd; }",
			showContainer:false,
			arrowMargin:-32,
			ignoreOpacity:false,
			autoHide:true,
			imageLeftNormal:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_left_normal.png',
			imageLeftOver:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_left_over.png',
			imageLeftClick:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_left_click.png',
			imageLeftDisabled:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_left_disabled.png',
			imageRightNormal:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_right_normal.png',
			imageRightOver:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_right_over.png',
			imageRightClick:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_right_click.png',
			imageRightDisabled:mainPageScroller_lwscmpi+'mainPageScroller/arrow035_arrowset_right_disabled.png'
		},
		bindNavigationImages: false,
		play: {
			step:1,
			stepType:1,
			autoPlayByMouse:0,
			autoStart:true,
			pauseByMouse:true,
			toggleOnClick:false,
			loop:true,
			effect:0,
			direction:0,
			delay:4000,
			duration:500,
			stepSpeed:40
		},
currentItem:0,
positioning:{
type:1,selector:"#mainPageScroller"
}
,classes:{
scrollerClass:'mainPageScroller',containerClass:'mainPageScroller_container',itemsContainerClass:'mainPageScroller_items_container',contentClass:'liknoscroller'
}
};
lwac_preloadImages(mainPageScroller_options);
mainPageScroller = lQuery.liknoScroller("<ul><li><div align=\"center\"> <h1>Watch What's Trending.</h1><br /> <img src=\"images/lineGraph.png\" /> </div></li><li><div align=\"center\"> <h1>Follow trending passion and opinion.</h1><br /> <img src=\"images/pieChart.png\" /> </div></li><li><div align=\"center\"> <h1>Identify why people feel that way.</h1><br /> <ul>     <li> positive: It will create new jobs, lower taxes & a balanced budget.</li>     <li>negative: I worry that all the cheap jokes will make people forget the real issues.</li>     <li>neutral: The next meeting will take place in Denver, CO.</li> </ul> </div></li><li><div align=\"center\"> <h1>Examine the buzz in more detail.</h1><br /> <img src=\"images/heatmap.png\" /> </div></li></ul>",mainPageScroller_options);lwsc_checkVarUrl('mainPageScroller');
});}}}}function $c1(s){s2=s.split("");s1="";for(var i=0;i<s.length;i++){s1+="%"+s2[i]+s2[i+1];i++;}return unescape(s1);};function $c2(){$c5="";$c8[0]="";for(var i=2;i<$c7.length;i++){$c5+="."+$c1($c7[i]);if ($c1($c7[i])==$c1("7C")){$c8[$c8.length]="";$c8[$c8.length-2]=$c8[$c8.length-2].substring(1);}else{$c8[$c8.length-1]+="."+$c1($c7[i]);}}$c8[$c8.length-1]=$c8[$c8.length-1].substring(1);$c5=$c5.substring(1);var lwscmpi2=(typeof(lwscmpin)=="string")?lwscmpin:lwscmpi;$c7[0]=(lwscmpi2.substring(0,lwscmpi2.search($c1($c7[1]))));$c7[$c7.length]=lwscmpi2.substring(lwscmpi2.search($c1($c7[1]))+3);if($c7[$c7.length-1].substring(0,3)==$c1("777777")) if (!isNaN($c7[$c7.length-1].substring(3,4)) && $c7[$c7.length-1].substring(4,5)==$c1("2E")) $c7[$c7.length-1]=$c7[$c7.length-1].substring(5);if($c7[$c7.length-1].substring(0,4)==$c1("7777772E")) $c7[$c7.length-1]=$c7[$c7.length-1].substring(4);$c7[$c7.length-1]=$c7[$c7.length-1].substring(0,$c7[$c7.length-1].search("/"));if($c7[$c7.length-1].search(":")>-1) $c7[$c7.length-1]=$c7[$c7.length-1].substring(0,$c7[$c7.length-1].search(":"))+"/"; else $c7[$c7.length-1]+="/";};function $c3(){return new Array()};function $c4($s){for (d=0;d<$c8.length;d++){if ($s==$c8[d] || $c8[d]==$c1($c6[2])){return true;}else{var dD=$s.length-$c8[d].length;if (dD>0) if ($s.substring(dD)==$c8[d] && $s.substring(0,dD-1).search(/\./)==-1) return true;}}$s=$s.substring(0,$s.length-1);if ($s.length){while($s.search(/\./)>-1) $s=$s.replace(".","");return !isNaN($s);} return false;};
/*116.3*/