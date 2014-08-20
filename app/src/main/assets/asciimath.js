function AMcreateElementXHTML(t){return AMnoNS?document.createElement(t):document.createElementNS("http://www.w3.org/1999/xhtml",t)}function AMnoMathMLNote(){var t=AMcreateElementXHTML("h3");t.setAttribute("align","center"),t.appendChild(AMcreateElementXHTML("p")),t.appendChild(document.createTextNode("To view the "));var e=AMcreateElementXHTML("a");return e.appendChild(document.createTextNode("ASCIIMathML")),e.setAttribute("href","http://www.chapman.edu/~jipsen/asciimath.html"),t.appendChild(e),t.appendChild(document.createTextNode(" notation use Internet Explorer 6+")),e=AMcreateElementXHTML("a"),e.appendChild(document.createTextNode("MathPlayer")),e.setAttribute("href","http://www.dessci.com/en/products/mathplayer/download.htm"),t.appendChild(e),t.appendChild(document.createTextNode(" or Netscape/Mozilla/Firefox")),t.appendChild(AMcreateElementXHTML("p")),t}function AMisMathMLavailable(){if(navigator.product&&"Gecko"==navigator.product){var t=navigator.userAgent.toLowerCase().match(/rv:\s*([\d\.]+)/);return null!=t&&(t=t[1].split("."),t.length<3&&(t[2]=0),t.length<2&&(t[1]=0)),null!=t&&1e4*t[0]+100*t[1]+1*t[2]>=10100?(AMisGecko=1e4*t[0]+100*t[1]+1*t[2],null):AMnoMathMLNote()}if("Microsoft"!=navigator.appName.slice(0,9))return AMnoMathMLNote();try{{new ActiveXObject("MathPlayer.Factory.1")}return null}catch(e){return AMnoMathMLNote()}}function compareNames(t,e){return t.input>e.input?1:-1}function AMinitSymbols(){var t,e=[];for(t=0;t<AMsymbols.length;t++)!AMsymbols[t].tex||"boolean"==typeof AMsymbols[t].notexcopy&&AMsymbols[t].notexcopy||(e[e.length]={input:AMsymbols[t].tex,tag:AMsymbols[t].tag,output:AMsymbols[t].output,ttype:AMsymbols[t].ttype});for(AMsymbols=AMsymbols.concat(e),AMsymbols.sort(compareNames),t=0;t<AMsymbols.length;t++)AMnames[t]=AMsymbols[t].input}function AMcreateElementMathML(t){return AMisIE?document.createElement("m:"+t):document.createElementNS(AMmathml,t)}function AMcreateMmlNode(t,e){if(AMisIE)var n=document.createElement("m:"+t);else var n=document.createElementNS(AMmathml,t);return n.appendChild(e),n}function newcommand(t,e){AMsymbols=AMsymbols.concat([{input:t,tag:"mo",output:e,tex:null,ttype:DEFINITION}])}function AMremoveCharsAndBlanks(t,e){var n;n="\\"==t.charAt(e)&&"\\"!=t.charAt(e+1)&&" "!=t.charAt(e+1)?t.slice(e+1):t.slice(e);for(var u=0;u<n.length&&n.charCodeAt(u)<=32;u+=1);return n.slice(u)}function AMposition(t,e,n){if(0==n){var u,o;for(n=-1,u=t.length;u>n+1;)o=n+u>>1,t[o]<e?n=o:u=o;return u}for(var a=n;a<t.length&&t[a]<e;a++);return a}function AMgetSymbol(t){for(var e,n,u,o=0,a=0,p="",l=!0,r=1;r<=t.length&&l;r++)n=t.slice(0,r),a=o,o=AMposition(AMnames,n,a),o<AMnames.length&&t.slice(0,AMnames[o].length)==AMnames[o]&&(p=AMnames[o],e=o,r=p.length),l=o<AMnames.length&&t.slice(0,AMnames[o].length)>=AMnames[o];if(AMpreviousSymbol=AMcurrentSymbol,""!=p)return AMcurrentSymbol=AMsymbols[e].ttype,AMsymbols[e];AMcurrentSymbol=CONST,o=1,n=t.slice(0,1);for(var i=!0;n>="0"&&"9">=n&&o<=t.length;)n=t.slice(o,o+1),o++;if(n==decimalsign&&(n=t.slice(o,o+1),n>="0"&&"9">=n))for(i=!1,o++;n>="0"&&"9">=n&&o<=t.length;)n=t.slice(o,o+1),o++;return i&&o>1||o>2?(n=t.slice(0,o-1),u="mn"):(o=2,n=t.slice(0,1),u=("A">n||n>"Z")&&("a">n||n>"z")?"mo":"mi"),"-"==n&&AMpreviousSymbol==INFIX?(AMcurrentSymbol=INFIX,{input:n,tag:u,output:n,ttype:UNARY,func:!0,val:!0}):{input:n,tag:u,output:n,ttype:CONST,val:!0}}function AMTremoveBrackets(t){var e;return"{"==t.charAt(0)&&"}"==t.charAt(t.length-1)&&(e=t.charAt(1),("("==e||"["==e)&&(t="{"+t.substr(2)),e=t.substr(1,6),("\\left("==e||"\\left["==e||"\\left{"==e)&&(t="{"+t.substr(7)),e=t.substr(1,12),("\\left\\lbrace"==e||"\\left\\langle"==e)&&(t="{"+t.substr(13)),e=t.charAt(t.length-2),(")"==e||"]"==e||"."==e)&&(t=t.substr(0,t.length-8)+"}"),e=t.substr(t.length-8,7),("\\rbrace"==e||"\\rangle"==e)&&(t=t.substr(0,t.length-14)+"}")),t}function AMremoveBrackets(t){var e;("mrow"==t.nodeName||"M:MROW"==t.nodeName)&&(e=t.firstChild.firstChild.nodeValue,("("==e||"["==e||"{"==e)&&t.removeChild(t.firstChild)),("mrow"==t.nodeName||"M:MROW"==t.nodeName)&&(e=t.lastChild.firstChild.nodeValue,(")"==e||"]"==e||"}"==e)&&t.removeChild(t.lastChild))}function AMTgetTeXsymbol(t){return pre="boolean"==typeof t.val&&t.val?"":"\\",null==t.tex?pre+(""==pre?t.input:t.input.toLowerCase()):pre+t.tex}function AMTgetTeXbracket(t){return null==t.tex?t.input:"\\"+t.tex}function AMTparseSexpr(t){var e,n,u,o,a,p="";if(t=AMremoveCharsAndBlanks(t,0),e=AMgetSymbol(t),null==e||e.ttype==RIGHTBRACKET&&AMnestingDepth>0)return[null,t];switch(e.ttype==DEFINITION&&(t=e.output+AMremoveCharsAndBlanks(t,e.input.length),e=AMgetSymbol(t)),e.ttype){case UNDEROVER:case CONST:t=AMremoveCharsAndBlanks(t,e.input.length);var l=AMTgetTeXsymbol(e);return"\\"==l.charAt(0)||"mo"==e.tag?[l,t]:["{"+l+"}",t];case LEFTBRACKET:return AMnestingDepth++,t=AMremoveCharsAndBlanks(t,e.input.length),u=AMTparseExpr(t,!0),AMnestingDepth--,"\\right"==u[0].substr(0,6)?(u[0]="\\right."==u[0].substr(0,7)?u[0].substr(7):u[0].substr(6),n="boolean"==typeof e.invisible&&e.invisible?"{"+u[0]+"}":"{"+AMTgetTeXbracket(e)+u[0]+"}"):n="boolean"==typeof e.invisible&&e.invisible?"{\\left."+u[0]+"}":"{\\left"+AMTgetTeXbracket(e)+u[0]+"}",[n,u[1]];case TEXT:return e!=AMquote&&(t=AMremoveCharsAndBlanks(t,e.input.length)),o="{"==t.charAt(0)?t.indexOf("}"):"("==t.charAt(0)?t.indexOf(")"):"["==t.charAt(0)?t.indexOf("]"):e==AMquote?t.slice(1).indexOf('"')+1:0,-1==o&&(o=t.length),a=t.slice(1,o)," "==a.charAt(0)&&(p="\\ "),p+="\\text{"+a+"}"," "==a.charAt(a.length-1)&&(p+="\\ "),t=AMremoveCharsAndBlanks(t,o+1),[p,t];case UNARY:return t=AMremoveCharsAndBlanks(t,e.input.length),u=AMTparseSexpr(t),null==u[0]?["{"+AMTgetTeXsymbol(e)+"}",t]:"boolean"==typeof e.func&&e.func?(a=t.charAt(0),"^"==a||"_"==a||"/"==a||"|"==a||","==a?["{"+AMTgetTeXsymbol(e)+"}",t]:(n="{"+AMTgetTeXsymbol(e)+"{"+u[0]+"}}",[n,u[1]])):(u[0]=AMTremoveBrackets(u[0]),"sqrt"==e.input?["\\sqrt{"+u[0]+"}",u[1]]:"boolean"==typeof e.acc&&e.acc?["{"+AMTgetTeXsymbol(e)+"{"+u[0]+"}}",u[1]]:["{"+AMTgetTeXsymbol(e)+"{"+u[0]+"}}",u[1]]);case BINARY:if(t=AMremoveCharsAndBlanks(t,e.input.length),u=AMTparseSexpr(t),null==u[0])return["{"+AMTgetTeXsymbol(e)+"}",t];u[0]=AMTremoveBrackets(u[0]);var r=AMTparseSexpr(u[1]);return null==r[0]?["{"+AMTgetTeXsymbol(e)+"}",t]:(r[0]=AMTremoveBrackets(r[0]),"color"==e.input&&(p="{\\color{"+u[0].replace(/[\{\}]/g,"")+"}"+r[0]+"}}"),("root"==e.input||"stackrel"==e.input)&&(p="root"==e.input?"{\\sqrt["+u[0]+"]{"+r[0]+"}}":"{"+AMTgetTeXsymbol(e)+"{"+u[0]+"}{"+r[0]+"}}"),"frac"==e.input&&(p="{\\frac{"+u[0]+"}{"+r[0]+"}}"),[p,r[1]]);case INFIX:return t=AMremoveCharsAndBlanks(t,e.input.length),[e.output,t];case SPACE:return t=AMremoveCharsAndBlanks(t,e.input.length),["{\\quad\\text{"+e.input+"}\\quad}",t];case LEFTRIGHT:AMnestingDepth++,t=AMremoveCharsAndBlanks(t,e.input.length),u=AMTparseExpr(t,!1),AMnestingDepth--;var a="";return a=u[0].charAt(u[0].length-1),"|"==a?(n="{\\left|"+u[0]+"}",[n,u[1]]):(n="{\\mid}",[n,t]);default:return t=AMremoveCharsAndBlanks(t,e.input.length),["{"+AMTgetTeXsymbol(e)+"}",t]}}function AMTparseIexpr(t){var e,n,u,o,a,p;if(t=AMremoveCharsAndBlanks(t,0),n=AMgetSymbol(t),a=AMTparseSexpr(t),o=a[0],t=a[1],e=AMgetSymbol(t),e.ttype==INFIX&&"/"!=e.input)if(t=AMremoveCharsAndBlanks(t,e.input.length),a=AMTparseSexpr(t),a[0]=null==a[0]?"{}":AMTremoveBrackets(a[0]),t=a[1],"_"==e.input)if(u=AMgetSymbol(t),p=n.ttype==UNDEROVER,"^"==u.input){t=AMremoveCharsAndBlanks(t,u.input.length);var l=AMTparseSexpr(t);l[0]=AMTremoveBrackets(l[0]),t=l[1],o="{"+o,o+="_{"+a[0]+"}",o+="^{"+l[0]+"}",o+="}"}else o+="_{"+a[0]+"}";else o="{"+o+"}^{"+a[0]+"}";return[o,t]}function AMTparseExpr(t,e){var n,u,o,a,p="",l=!1;do t=AMremoveCharsAndBlanks(t,0),o=AMTparseIexpr(t),u=o[0],t=o[1],n=AMgetSymbol(t),n.ttype==INFIX&&"/"==n.input?(t=AMremoveCharsAndBlanks(t,n.input.length),o=AMTparseIexpr(t),o[0]=null==o[0]?"{}":AMTremoveBrackets(o[0]),t=o[1],u=AMTremoveBrackets(u),u="\\frac{"+u+"}",u+="{"+o[0]+"}",p+=u,n=AMgetSymbol(t)):void 0!=u&&(p+=u);while((n.ttype!=RIGHTBRACKET&&(n.ttype!=LEFTRIGHT||e)||0==AMnestingDepth)&&null!=n&&""!=n.output);if(n.ttype==RIGHTBRACKET||n.ttype==LEFTRIGHT){var r=p.length;if(r>2&&"{"==p.charAt(0)&&p.indexOf(",")>0){var i=p.charAt(r-2);if(")"==i||"]"==i){var m=p.charAt(6);if("("==m&&")"==i&&"}"!=n.output||"["==m&&"]"==i){var s="\\matrix{",c=new Array;c.push(0);var d=!0,A=0,g=[];g[0]=[0];var M=0,h=0;for(a=1;r-1>a;a++)p.charAt(a)==m&&A++,p.charAt(a)==i&&(A--,0==A&&","==p.charAt(a+2)&&"{"==p.charAt(a+3)&&(c.push(a+2),M=a+2,g[M]=[a+2])),("["==p.charAt(a)||"("==p.charAt(a))&&h++,("]"==p.charAt(a)||")"==p.charAt(a))&&h--,","==p.charAt(a)&&1==h&&g[M].push(a);c.push(r);var y=-1;if(0==A&&c.length>0)for(a=0;a<c.length-1;a++){if(a>0&&(s+="\\\\"),0==a)if(1==g[c[a]].length)var N=[p.substr(c[a]+7,c[a+1]-c[a]-15)];else{for(var N=[p.substring(c[a]+7,g[c[a]][1])],x=2;x<g[c[a]].length;x++)N.push(p.substring(g[c[a]][x-1]+1,g[c[a]][x]));N.push(p.substring(g[c[a]][g[c[a]].length-1]+1,c[a+1]-8))}else if(1==g[c[a]].length)var N=[p.substr(c[a]+8,c[a+1]-c[a]-16)];else{for(var N=[p.substring(c[a]+8,g[c[a]][1])],x=2;x<g[c[a]].length;x++)N.push(p.substring(g[c[a]][x-1]+1,g[c[a]][x]));N.push(p.substring(g[c[a]][g[c[a]].length-1]+1,c[a+1]-8))}y>0&&N.length!=y?d=!1:-1==y&&(y=N.length),s+=N.join("&")}s+="}",d&&(p=s)}}}t=AMremoveCharsAndBlanks(t,n.input.length),"boolean"==typeof n.invisible&&n.invisible?(p+="\\right.",l=!0):(u="\\right"+AMTgetTeXbracket(n),p+=u,l=!0)}return AMnestingDepth>0&&!l&&(p+="\\right."),[p,t]}function AMTparseAMtoTeX(t){return AMnestingDepth=0,t=t.replace(/(&nbsp;|\u00a0|&#160;)/g,""),t=t.replace(/&gt;/g,">"),t=t.replace(/&lt;/g,"<"),null==t.match(/\S/)?"":AMTparseExpr(t.replace(/^\s+/g,""),!1)[0]}function AMTparseMath(t){if(t=t.replace(/(&nbsp;|\u00a0|&#160;)/g,""),t=t.replace(/&gt;/g,">"),t=t.replace(/&lt;/g,"<"),null==t.match(/\S/))return document.createTextNode(" ");var e=AMTparseAMtoTeX(t);"undefined"!=typeof mathbg&&"dark"==mathbg&&(e="\\reverse "+e),""!=mathcolor&&(e="\\"+mathcolor+e),e=displaystyle?"\\displaystyle"+e:"\\textstyle"+e,e=e.replace("$","\\$");var n=AMcreateElementXHTML("img");return e="function"==typeof encodeURIComponent?encodeURIComponent(e):escape(e),n.src=AMTcgiloc+"?"+e,n.style.verticalAlign="middle",showasciiformulaonhover&&n.setAttribute("title",t.replace(/\s+/g," ")),n}function AMparseSexpr(t){var e,n,u,o,a,p=document.createDocumentFragment();if(t=AMremoveCharsAndBlanks(t,0),e=AMgetSymbol(t),null==e||e.ttype==RIGHTBRACKET&&AMnestingDepth>0)return[null,t];switch(e.ttype==DEFINITION&&(t=e.output+AMremoveCharsAndBlanks(t,e.input.length),e=AMgetSymbol(t)),e.ttype){case UNDEROVER:case CONST:return t=AMremoveCharsAndBlanks(t,e.input.length),[AMcreateMmlNode(e.tag,document.createTextNode(e.output)),t];case LEFTBRACKET:return AMnestingDepth++,t=AMremoveCharsAndBlanks(t,e.input.length),u=AMparseExpr(t,!0),AMnestingDepth--,"boolean"==typeof e.invisible&&e.invisible?n=AMcreateMmlNode("mrow",u[0]):(n=AMcreateMmlNode("mo",document.createTextNode(e.output)),n=AMcreateMmlNode("mrow",n),n.appendChild(u[0])),[n,u[1]];case TEXT:return e!=AMquote&&(t=AMremoveCharsAndBlanks(t,e.input.length)),o="{"==t.charAt(0)?t.indexOf("}"):"("==t.charAt(0)?t.indexOf(")"):"["==t.charAt(0)?t.indexOf("]"):e==AMquote?t.slice(1).indexOf('"')+1:0,-1==o&&(o=t.length),a=t.slice(1,o)," "==a.charAt(0)&&(n=AMcreateElementMathML("mspace"),n.setAttribute("width","1ex"),p.appendChild(n)),p.appendChild(AMcreateMmlNode(e.tag,document.createTextNode(a)))," "==a.charAt(a.length-1)&&(n=AMcreateElementMathML("mspace"),n.setAttribute("width","1ex"),p.appendChild(n)),t=AMremoveCharsAndBlanks(t,o+1),[AMcreateMmlNode("mrow",p),t];case UNARY:if(t=AMremoveCharsAndBlanks(t,e.input.length),u=AMparseSexpr(t),null==u[0])return[AMcreateMmlNode(e.tag,document.createTextNode(e.output)),t];if("boolean"==typeof e.func&&e.func)return a=t.charAt(0),"^"==a||"_"==a||"/"==a||"|"==a||","==a?[AMcreateMmlNode(e.tag,document.createTextNode(e.output)),t]:(n=AMcreateMmlNode("mrow",AMcreateMmlNode(e.tag,document.createTextNode(e.output))),n.appendChild(u[0]),[n,u[1]]);if(AMremoveBrackets(u[0]),"sqrt"==e.input)return[AMcreateMmlNode(e.tag,u[0]),u[1]];if("boolean"==typeof e.acc&&e.acc)return n=AMcreateMmlNode(e.tag,u[0]),n.appendChild(AMcreateMmlNode("mo",document.createTextNode(e.output))),[n,u[1]];if(!AMisIE&&"undefined"!=typeof e.codes)for(o=0;o<u[0].childNodes.length;o++)if("mi"==u[0].childNodes[o].nodeName||"mi"==u[0].nodeName){a="mi"==u[0].nodeName?u[0].firstChild.nodeValue:u[0].childNodes[o].firstChild.nodeValue;for(var l=[],r=0;r<a.length;r++)l+=a.charCodeAt(r)>64&&a.charCodeAt(r)<91?String.fromCharCode(e.codes[a.charCodeAt(r)-65]):a.charAt(r);"mi"==u[0].nodeName?u[0]=AMcreateElementMathML("mo").appendChild(document.createTextNode(l)):u[0].replaceChild(AMcreateElementMathML("mo").appendChild(document.createTextNode(l)),u[0].childNodes[o])}return n=AMcreateMmlNode(e.tag,u[0]),n.setAttribute(e.atname,e.atval),[n,u[1]];case BINARY:if(t=AMremoveCharsAndBlanks(t,e.input.length),u=AMparseSexpr(t),null==u[0])return[AMcreateMmlNode("mo",document.createTextNode(e.input)),t];AMremoveBrackets(u[0]);var i=AMparseSexpr(u[1]);return null==i[0]?[AMcreateMmlNode("mo",document.createTextNode(e.input)),t]:(AMremoveBrackets(i[0]),"color"==e.input?("{"==t.charAt(0)?o=t.indexOf("}"):"("==t.charAt(0)?o=t.indexOf(")"):"["==t.charAt(0)&&(o=t.indexOf("]")),a=t.slice(1,o),n=AMcreateMmlNode(e.tag,i[0]),n.setAttribute("color",a),[n,i[1]]):(("root"==e.input||"stackrel"==e.input)&&p.appendChild(i[0]),p.appendChild(u[0]),"frac"==e.input&&p.appendChild(i[0]),[AMcreateMmlNode(e.tag,p),i[1]]));case INFIX:return t=AMremoveCharsAndBlanks(t,e.input.length),[AMcreateMmlNode("mo",document.createTextNode(e.output)),t];case SPACE:return t=AMremoveCharsAndBlanks(t,e.input.length),n=AMcreateElementMathML("mspace"),n.setAttribute("width","1ex"),p.appendChild(n),p.appendChild(AMcreateMmlNode(e.tag,document.createTextNode(e.output))),n=AMcreateElementMathML("mspace"),n.setAttribute("width","1ex"),p.appendChild(n),[AMcreateMmlNode("mrow",p),t];case LEFTRIGHT:AMnestingDepth++,t=AMremoveCharsAndBlanks(t,e.input.length),u=AMparseExpr(t,!1),AMnestingDepth--;var a="";return null!=u[0].lastChild&&(a=u[0].lastChild.firstChild.nodeValue),"|"==a?(n=AMcreateMmlNode("mo",document.createTextNode(e.output)),n=AMcreateMmlNode("mrow",n),n.appendChild(u[0]),[n,u[1]]):(n=AMcreateMmlNode("mo",document.createTextNode(e.output)),n=AMcreateMmlNode("mrow",n),[n,t]);default:return t=AMremoveCharsAndBlanks(t,e.input.length),[AMcreateMmlNode(e.tag,document.createTextNode(e.output)),t]}}function AMparseIexpr(t){var e,n,u,o,a,p;if(t=AMremoveCharsAndBlanks(t,0),n=AMgetSymbol(t),a=AMparseSexpr(t),o=a[0],t=a[1],e=AMgetSymbol(t),e.ttype==INFIX&&"/"!=e.input)if(t=AMremoveCharsAndBlanks(t,e.input.length),a=AMparseSexpr(t),null==a[0]?a[0]=AMcreateMmlNode("mo",document.createTextNode("□")):AMremoveBrackets(a[0]),t=a[1],"_"==e.input)if(u=AMgetSymbol(t),p=n.ttype==UNDEROVER,"^"==u.input){t=AMremoveCharsAndBlanks(t,u.input.length);var l=AMparseSexpr(t);AMremoveBrackets(l[0]),t=l[1],o=AMcreateMmlNode(p?"munderover":"msubsup",o),o.appendChild(a[0]),o.appendChild(l[0]),o=AMcreateMmlNode("mrow",o)}else o=AMcreateMmlNode(p?"munder":"msub",o),o.appendChild(a[0]);else o=AMcreateMmlNode(e.tag,o),o.appendChild(a[0]);return[o,t]}function AMparseExpr(t,e){var n,u,o,a,p=document.createDocumentFragment();do t=AMremoveCharsAndBlanks(t,0),o=AMparseIexpr(t),u=o[0],t=o[1],n=AMgetSymbol(t),n.ttype==INFIX&&"/"==n.input?(t=AMremoveCharsAndBlanks(t,n.input.length),o=AMparseIexpr(t),null==o[0]?o[0]=AMcreateMmlNode("mo",document.createTextNode("□")):AMremoveBrackets(o[0]),t=o[1],AMremoveBrackets(u),u=AMcreateMmlNode(n.tag,u),u.appendChild(o[0]),p.appendChild(u),n=AMgetSymbol(t)):void 0!=u&&p.appendChild(u);while((n.ttype!=RIGHTBRACKET&&(n.ttype!=LEFTRIGHT||e)||0==AMnestingDepth)&&null!=n&&""!=n.output);if(n.ttype==RIGHTBRACKET||n.ttype==LEFTRIGHT){var l=p.childNodes.length;if(l>0&&"mrow"==p.childNodes[l-1].nodeName&&l>1&&"mo"==p.childNodes[l-2].nodeName&&","==p.childNodes[l-2].firstChild.nodeValue){var r=p.childNodes[l-1].lastChild.firstChild.nodeValue;if(")"==r||"]"==r){var i=p.childNodes[l-1].firstChild.firstChild.nodeValue;if("("==i&&")"==r&&"}"!=n.output||"["==i&&"]"==r){var m=[],s=!0,c=p.childNodes.length;for(a=0;s&&c>a;a+=2){if(m[a]=[],u=p.childNodes[a],s&&(s="mrow"==u.nodeName&&(a==c-1||"mo"==u.nextSibling.nodeName&&","==u.nextSibling.firstChild.nodeValue)&&u.firstChild.firstChild.nodeValue==i&&u.lastChild.firstChild.nodeValue==r),s)for(var d=0;d<u.childNodes.length;d++)","==u.childNodes[d].firstChild.nodeValue&&(m[a][m[a].length]=d);s&&a>1&&(s=m[a].length==m[a-2].length)}if(s){var A,g,M,h,y=document.createDocumentFragment();for(a=0;c>a;a+=2){for(A=document.createDocumentFragment(),g=document.createDocumentFragment(),u=p.firstChild,M=u.childNodes.length,h=0,u.removeChild(u.firstChild),d=1;M-1>d;d++)"undefined"!=typeof m[a][h]&&d==m[a][h]?(u.removeChild(u.firstChild),A.appendChild(AMcreateMmlNode("mtd",g)),h++):g.appendChild(u.firstChild);A.appendChild(AMcreateMmlNode("mtd",g)),p.childNodes.length>2&&(p.removeChild(p.firstChild),p.removeChild(p.firstChild)),y.appendChild(AMcreateMmlNode("mtr",A))}u=AMcreateMmlNode("mtable",y),"boolean"==typeof n.invisible&&n.invisible&&u.setAttribute("columnalign","left"),p.replaceChild(u,p.firstChild)}}}}t=AMremoveCharsAndBlanks(t,n.input.length),"boolean"==typeof n.invisible&&n.invisible||(u=AMcreateMmlNode("mo",document.createTextNode(n.output)),p.appendChild(u))}return[p,t]}function AMparseMath(t){var e=AMcreateElementMathML("mstyle");if(""!=mathcolor&&e.setAttribute("mathcolor",mathcolor),displaystyle&&e.setAttribute("displaystyle","true"),""!=mathfontfamily&&e.setAttribute("fontfamily",mathfontfamily),AMnestingDepth=0,t=t.replace(/&nbsp;/g,""),t=t.replace(/&gt;/g,">"),t=t.replace(/&lt;/g,"<"),e.appendChild(AMparseExpr(t.replace(/^\s+/g,""),!1)[0]),e=AMcreateMmlNode("math",e),showasciiformulaonhover&&e.setAttribute("title",t.replace(/\s+/g," ")),""!=mathfontfamily&&(AMisIE||"serif"!=mathfontfamily)){var n=AMcreateElementXHTML("font");return n.setAttribute("face",mathfontfamily),n.appendChild(e),n}return e}function AMstrarr2docFrag(t,e,n){for(var u=document.createDocumentFragment(),o=!1,a=0;a<t.length;a++){if(o&&!n)u.appendChild(AMTparseMath(t[a]));else if(o&&n)u.appendChild(AMparseMath(t[a]));else{var p=e?t[a].split("\n\n"):[t[a]];u.appendChild(AMcreateElementXHTML("span").appendChild(document.createTextNode(p[0])));for(var l=1;l<p.length;l++)u.appendChild(AMcreateElementXHTML("p")),u.appendChild(AMcreateElementXHTML("span").appendChild(document.createTextNode(p[l])))}o=!o}return u}function AMprocessNodeR(t,e){var n,u,o,a,p;if(0==t.childNodes.length){if(8==t.nodeType&&!e||"form"==t.parentNode.nodeName||"FORM"==t.parentNode.nodeName||"textarea"==t.parentNode.nodeName||"TEXTAREA"==t.parentNode.nodeName||"pre"==t.parentNode.nodeName||"PRE"==t.parentNode.nodeName)return 0;if(u=t.nodeValue,null!=u){for(u=u.replace(/\r\n\r\n/g,"\n\n"),doubleblankmathdelimiter&&(u=u.replace(/\x20\x20\./g," "+AMdelimiter1+"."),u=u.replace(/\x20\x20,/g," "+AMdelimiter1+","),u=u.replace(/\x20\x20/g," "+AMdelimiter1+" ")),u=u.replace(/\x20+/g," "),u=u.replace(/\s*\r\n/g," "),n=!1,AMusedelimiter2&&(u=u.replace(new RegExp(AMescape2,"g"),function(){return n=!0,"AMescape2"})),u=u.replace(new RegExp(AMescape1,"g"),function(){return n=!0,"AMescape1"}),AMusedelimiter2&&(u=u.replace(new RegExp(AMdelimiter2regexp,"g"),AMdelimiter1)),o=u.split(AMdelimiter1),p=0;p<o.length;p++)o[p]=AMusedelimiter2?o[p].replace(/AMescape2/g,AMdelimiter2).replace(/AMescape1/g,AMdelimiter1):o[p].replace(/AMescape1/g,AMdelimiter1);if(o.length>1||n){if(checkForMathML){checkForMathML=!1;var l=AMisMathMLavailable();AMnoMathML=null!=l,AMnoMathML&&notifyIfNoMathML&&(alertIfNoMathML?alert("To view the ASCIIMathML notation use Internet Explorer 6 +\nMathPlayer (free from www.dessci.com)\n                or Firefox/Mozilla/Netscape"):AMbody.insertBefore(l,AMbody.childNodes[0]))}a=AMnoMathML?AMstrarr2docFrag(o,8==t.nodeType,!1):AMstrarr2docFrag(o,8==t.nodeType,!0);var r=a.childNodes.length;return t.parentNode.replaceChild(a,t),r-1}}}else if("math"!=t.nodeName)for(p=0;p<t.childNodes.length;p++)p+=AMprocessNodeR(t.childNodes[p],e);return 0}function AMprocessNode(t,e,n){var u,o;if(null!=n){u=document.getElementsByTagName("span");for(var a=0;a<u.length;a++)"AM"==u[a].className&&AMprocessNodeR(u[a],e)}else{try{o=t.innerHTML}catch(p){}AMusedelimiter2?(null==o||-1!=o.indexOf(AMdelimiter1)||-1!=o.indexOf(AMdelimiter2))&&AMprocessNodeR(t,e):(null==o||-1!=o.indexOf(AMdelimiter1))&&AMprocessNodeR(t,e)}if(AMisIE){u=document.getElementsByTagName("math");for(var a=0;a<u.length;a++)u[a].update()}}function translate(t){AMtranslated||(AMtranslated=!0,AMbody=document.getElementsByTagName("body")[0],AMprocessNode(AMbody,!1,t))}function AMBBoxFor(t){document.getElementById("hidden").innerHTML='<nobr><span class="typeset"><span class="scale">'+t+"</span></span></nobr>";var e={w:document.getElementById("hidden").offsetWidth,h:document.getElementById("hidden").offsetHeight};return document.getElementById("hidden").innerHTML="",e}function AMcheckTeX(){hiddendiv=document.createElement("div"),hiddendiv.style.visibility="hidden",hiddendiv.id="hidden",document.body.appendChild(hiddendiv),wh=10900>AMisGecko?AMBBoxFor('<span style="font-family: STIXgeneral, cmex10, serif">&#xEFE8;</span>'):AMBBoxFor('<span style="font-family: STIXgeneral, serif">&#xEFE8;</span>'),wh2=AMBBoxFor('<span style="font-family: serif">&#xEFE8;</span>'),nofonts=wh.w==wh2.w&&wh.h==wh2.h,nofonts?(AMnoMathML=!0,AMnoFonts=!0):(AMnoMathML=!1,AMnoFonts=!1)}function generic(){AMnoMathML&&"undefined"!=typeof waitforAMTcgiloc&&null==AMTcgiloc?setTimeout("generic()",50):(!AMnoMathML&&AMisGecko>0&&AMcheckTeX(),translate())}var AMTcgiloc=null,checkForMathML=!0,notifyIfNoMathML=!1,alertIfNoMathML=!1,mathcolor="",mathfontfamily="Times,STIXGeneral,serif",displaystyle=!0,showasciiformulaonhover=!0,decimalsign=".",AMdelimiter1="`",AMescape1="\\\\`",AMusedelimiter2=!1,AMdelimiter2="$",AMescape2="\\\\\\$",AMdelimiter2regexp="\\$",doubleblankmathdelimiter=!1,AMisIE="Microsoft"==navigator.appName.slice(0,9),AMnoNS=null==document.createElementNS;null==document.getElementById&&alert("This webpage requires a recent browser such as\nMozilla/Netscape 7+ or Internet Explorer 6+MathPlayer");var AMcal=[61237,8492,61238,61239,8496,8497,61240,8459,8464,61241,61242,8466,8499,61243,61244,61245,61246,8475,61247,61248,61249,61250,61251,61252,61253,61254],AMfrk=[61277,61278,8493,61279,61280,61281,61282,8460,8465,61283,61284,61285,61286,61287,61288,61289,61290,8476,61291,61292,61293,61294,61295,61296,61297,8488],AMbbb=[61324,61325,8450,61326,61327,61328,61329,8461,61330,61331,61332,61333,61334,8469,61335,8473,8474,8477,61336,61337,61338,61339,61340,61341,61342,8484],CONST=0,UNARY=1,BINARY=2,INFIX=3,LEFTBRACKET=4,RIGHTBRACKET=5,SPACE=6,UNDEROVER=7,DEFINITION=8,LEFTRIGHT=9,TEXT=10,AMsqrt={input:"sqrt",tag:"msqrt",output:"sqrt",tex:null,ttype:UNARY},AMroot={input:"root",tag:"mroot",output:"root",tex:null,ttype:BINARY},AMfrac={input:"frac",tag:"mfrac",output:"/",tex:null,ttype:BINARY},AMdiv={input:"/",tag:"mfrac",output:"/",tex:null,ttype:INFIX},AMover={input:"stackrel",tag:"mover",output:"stackrel",tex:null,ttype:BINARY},AMsub={input:"_",tag:"msub",output:"_",tex:null,ttype:INFIX},AMsup={input:"^",tag:"msup",output:"^",tex:null,ttype:INFIX},AMtext={input:"text",tag:"mtext",output:"text",tex:null,ttype:TEXT},AMmbox={input:"mbox",tag:"mtext",output:"mbox",tex:null,ttype:TEXT},AMquote={input:'"',tag:"mtext",output:"mbox",tex:null,ttype:TEXT},AMsymbols=[{input:"alpha",tag:"mi",output:"α",tex:null,ttype:CONST},{input:"beta",tag:"mi",output:"β",tex:null,ttype:CONST},{input:"chi",tag:"mi",output:"χ",tex:null,ttype:CONST},{input:"delta",tag:"mi",output:"δ",tex:null,ttype:CONST},{input:"Delta",tag:"mo",output:"Δ",tex:null,ttype:CONST},{input:"epsi",tag:"mi",output:"ε",tex:"epsilon",ttype:CONST},{input:"varepsilon",tag:"mi",output:"ɛ",tex:null,ttype:CONST},{input:"eta",tag:"mi",output:"η",tex:null,ttype:CONST},{input:"gamma",tag:"mi",output:"γ",tex:null,ttype:CONST},{input:"Gamma",tag:"mo",output:"Γ",tex:null,ttype:CONST},{input:"iota",tag:"mi",output:"ι",tex:null,ttype:CONST},{input:"kappa",tag:"mi",output:"κ",tex:null,ttype:CONST},{input:"lambda",tag:"mi",output:"λ",tex:null,ttype:CONST},{input:"Lambda",tag:"mo",output:"Λ",tex:null,ttype:CONST},{input:"mu",tag:"mi",output:"μ",tex:null,ttype:CONST},{input:"nu",tag:"mi",output:"ν",tex:null,ttype:CONST},{input:"omega",tag:"mi",output:"ω",tex:null,ttype:CONST},{input:"Omega",tag:"mo",output:"Ω",tex:null,ttype:CONST},{input:"phi",tag:"mi",output:"φ",tex:null,ttype:CONST},{input:"varphi",tag:"mi",output:"ϕ",tex:null,ttype:CONST},{input:"Phi",tag:"mo",output:"Φ",tex:null,ttype:CONST},{input:"pi",tag:"mi",output:"π",tex:null,ttype:CONST},{input:"Pi",tag:"mo",output:"Π",tex:null,ttype:CONST},{input:"psi",tag:"mi",output:"ψ",tex:null,ttype:CONST},{input:"Psi",tag:"mi",output:"Ψ",tex:null,ttype:CONST},{input:"rho",tag:"mi",output:"ρ",tex:null,ttype:CONST},{input:"sigma",tag:"mi",output:"σ",tex:null,ttype:CONST},{input:"Sigma",tag:"mo",output:"Σ",tex:null,ttype:CONST},{input:"tau",tag:"mi",output:"τ",tex:null,ttype:CONST},{input:"theta",tag:"mi",output:"θ",tex:null,ttype:CONST},{input:"vartheta",tag:"mi",output:"ϑ",tex:null,ttype:CONST},{input:"Theta",tag:"mo",output:"Θ",tex:null,ttype:CONST},{input:"upsilon",tag:"mi",output:"υ",tex:null,ttype:CONST},{input:"xi",tag:"mi",output:"ξ",tex:null,ttype:CONST},{input:"Xi",tag:"mo",output:"Ξ",tex:null,ttype:CONST},{input:"zeta",tag:"mi",output:"ζ",tex:null,ttype:CONST},{input:"*",tag:"mo",output:"⋅",tex:"cdot",ttype:CONST},{input:"**",tag:"mo",output:"⋆",tex:"star",ttype:CONST},{input:"//",tag:"mo",output:"/",tex:null,ttype:CONST},{input:"\\\\",tag:"mo",output:"\\",tex:"backslash",ttype:CONST},{input:"setminus",tag:"mo",output:"\\",tex:null,ttype:CONST},{input:"xx",tag:"mo",output:"×",tex:"times",ttype:CONST},{input:"-:",tag:"mo",output:"÷",tex:"div",ttype:CONST},{input:"divide",tag:"mo",output:"-:",tex:null,ttype:DEFINITION},{input:"@",tag:"mo",output:"∘",tex:"circ",ttype:CONST},{input:"o+",tag:"mo",output:"⊕",tex:"oplus",ttype:CONST},{input:"ox",tag:"mo",output:"⊗",tex:"otimes",ttype:CONST},{input:"o.",tag:"mo",output:"⊙",tex:"odot",ttype:CONST},{input:"sum",tag:"mo",output:"∑",tex:null,ttype:UNDEROVER},{input:"prod",tag:"mo",output:"∏",tex:null,ttype:UNDEROVER},{input:"^^",tag:"mo",output:"∧",tex:"wedge",ttype:CONST},{input:"^^^",tag:"mo",output:"⋀",tex:"bigwedge",ttype:UNDEROVER},{input:"vv",tag:"mo",output:"∨",tex:"vee",ttype:CONST},{input:"vvv",tag:"mo",output:"⋁",tex:"bigvee",ttype:UNDEROVER},{input:"nn",tag:"mo",output:"∩",tex:"cap",ttype:CONST},{input:"nnn",tag:"mo",output:"⋂",tex:"bigcap",ttype:UNDEROVER},{input:"uu",tag:"mo",output:"∪",tex:"cup",ttype:CONST},{input:"uuu",tag:"mo",output:"⋃",tex:"bigcup",ttype:UNDEROVER},{input:"!=",tag:"mo",output:"≠",tex:"ne",ttype:CONST},{input:":=",tag:"mo",output:":=",tex:null,ttype:CONST},{input:"lt",tag:"mo",output:"<",tex:null,ttype:CONST},{input:"gt",tag:"mo",output:">",tex:null,ttype:CONST},{input:"<=",tag:"mo",output:"≤",tex:"le",ttype:CONST},{input:"lt=",tag:"mo",output:"≤",tex:"leq",ttype:CONST},{input:"gt=",tag:"mo",output:"≥",tex:"geq",ttype:CONST},{input:">=",tag:"mo",output:"≥",tex:"ge",ttype:CONST},{input:"geq",tag:"mo",output:"≥",tex:null,ttype:CONST},{input:"-<",tag:"mo",output:"≺",tex:"prec",ttype:CONST},{input:"-lt",tag:"mo",output:"≺",tex:null,ttype:CONST},{input:">-",tag:"mo",output:"≻",tex:"succ",ttype:CONST},{input:"-<=",tag:"mo",output:"⪯",tex:"preceq",ttype:CONST},{input:">-=",tag:"mo",output:"⪰",tex:"succeq",ttype:CONST},{input:"in",tag:"mo",output:"∈",tex:null,ttype:CONST},{input:"!in",tag:"mo",output:"∉",tex:"notin",ttype:CONST},{input:"sub",tag:"mo",output:"⊂",tex:"subset",ttype:CONST},{input:"sup",tag:"mo",output:"⊃",tex:"supset",ttype:CONST},{input:"sube",tag:"mo",output:"⊆",tex:"subseteq",ttype:CONST},{input:"supe",tag:"mo",output:"⊇",tex:"supseteq",ttype:CONST},{input:"-=",tag:"mo",output:"≡",tex:"equiv",ttype:CONST},{input:"~=",tag:"mo",output:"≅",tex:"stackrel{\\sim}{=}",ttype:CONST},{input:"cong",tag:"mo",output:"~=",tex:null,ttype:DEFINITION},{input:"~~",tag:"mo",output:"≈",tex:"approx",ttype:CONST},{input:"prop",tag:"mo",output:"∝",tex:"propto",ttype:CONST},{input:"and",tag:"mtext",output:"and",tex:null,ttype:SPACE},{input:"or",tag:"mtext",output:"or",tex:null,ttype:SPACE},{input:"not",tag:"mo",output:"¬",tex:"neg",ttype:CONST},{input:"=>",tag:"mo",output:"⇒",tex:"Rightarrow",ttype:CONST},{input:"implies",tag:"mo",output:"=>",tex:null,ttype:DEFINITION},{input:"if",tag:"mo",output:"if",tex:null,ttype:SPACE},{input:"<=>",tag:"mo",output:"⇔",tex:"Leftrightarrow",ttype:CONST},{input:"iff",tag:"mo",output:"<=>",tex:null,ttype:DEFINITION},{input:"AA",tag:"mo",output:"∀",tex:"forall",ttype:CONST},{input:"EE",tag:"mo",output:"∃",tex:"exists",ttype:CONST},{input:"_|_",tag:"mo",output:"⊥",tex:"bot",ttype:CONST},{input:"TT",tag:"mo",output:"⊤",tex:"top",ttype:CONST},{input:"|--",tag:"mo",output:"⊢",tex:"vdash",ttype:CONST},{input:"|==",tag:"mo",output:"⊨",tex:"models",ttype:CONST},{input:"(",tag:"mo",output:"(",tex:null,ttype:LEFTBRACKET},{input:")",tag:"mo",output:")",tex:null,ttype:RIGHTBRACKET},{input:"[",tag:"mo",output:"[",tex:null,ttype:LEFTBRACKET},{input:"]",tag:"mo",output:"]",tex:null,ttype:RIGHTBRACKET},{input:"{",tag:"mo",output:"{",tex:"lbrace",ttype:LEFTBRACKET},{input:"}",tag:"mo",output:"}",tex:"rbrace",ttype:RIGHTBRACKET},{input:"|",tag:"mo",output:"|",tex:null,ttype:LEFTRIGHT},{input:"(:",tag:"mo",output:"〈",tex:"langle",ttype:LEFTBRACKET},{input:":)",tag:"mo",output:"〉",tex:"rangle",ttype:RIGHTBRACKET},{input:"<<",tag:"mo",output:"〈",tex:"langle",ttype:LEFTBRACKET},{input:">>",tag:"mo",output:"〉",tex:"rangle",ttype:RIGHTBRACKET},{input:"{:",tag:"mo",output:"{:",tex:null,ttype:LEFTBRACKET,invisible:!0},{input:":}",tag:"mo",output:":}",tex:null,ttype:RIGHTBRACKET,invisible:!0},{input:"int",tag:"mo",output:"∫",tex:null,ttype:CONST},{input:"dx",tag:"mi",output:"{:d x:}",tex:null,ttype:DEFINITION},{input:"dy",tag:"mi",output:"{:d y:}",tex:null,ttype:DEFINITION},{input:"dz",tag:"mi",output:"{:d z:}",tex:null,ttype:DEFINITION},{input:"dt",tag:"mi",output:"{:d t:}",tex:null,ttype:DEFINITION},{input:"oint",tag:"mo",output:"∮",tex:null,ttype:CONST},{input:"del",tag:"mo",output:"∂",tex:"partial",ttype:CONST},{input:"grad",tag:"mo",output:"∇",tex:"nabla",ttype:CONST},{input:"+-",tag:"mo",output:"±",tex:"pm",ttype:CONST},{input:"O/",tag:"mo",output:"∅",tex:"emptyset",ttype:CONST},{input:"oo",tag:"mo",output:"∞",tex:"infty",ttype:CONST},{input:"aleph",tag:"mo",output:"ℵ",tex:null,ttype:CONST},{input:"...",tag:"mo",output:"...",tex:"ldots",ttype:CONST},{input:":.",tag:"mo",output:"∴",tex:"therefore",ttype:CONST},{input:"/_",tag:"mo",output:"∠",tex:"angle",ttype:CONST},{input:"\\ ",tag:"mo",output:" ",tex:null,ttype:CONST,val:!0},{input:"quad",tag:"mo",output:"  ",tex:null,ttype:CONST},{input:"qquad",tag:"mo",output:"    ",tex:null,ttype:CONST},{input:"cdots",tag:"mo",output:"⋯",tex:null,ttype:CONST},{input:"vdots",tag:"mo",output:"⋮",tex:null,ttype:CONST},{input:"ddots",tag:"mo",output:"⋱",tex:null,ttype:CONST},{input:"diamond",tag:"mo",output:"⋄",tex:null,ttype:CONST},{input:"square",tag:"mo",output:"□",tex:"boxempty",ttype:CONST},{input:"|__",tag:"mo",output:"⌊",tex:"lfloor",ttype:CONST},{input:"__|",tag:"mo",output:"⌋",tex:"rfloor",ttype:CONST},{input:"|~",tag:"mo",output:"⌈",tex:"lceil",ttype:CONST},{input:"lceiling",tag:"mo",output:"|~",tex:null,ttype:DEFINITION},{input:"~|",tag:"mo",output:"⌉",tex:"rceil",ttype:CONST},{input:"rceiling",tag:"mo",output:"~|",tex:null,ttype:DEFINITION},{input:"CC",tag:"mo",output:"ℂ",tex:"mathbb{C}",ttype:CONST,notexcopy:!0},{input:"NN",tag:"mo",output:"ℕ",tex:"mathbb{N}",ttype:CONST,notexcopy:!0},{input:"QQ",tag:"mo",output:"ℚ",tex:"mathbb{Q}",ttype:CONST,notexcopy:!0},{input:"RR",tag:"mo",output:"ℝ",tex:"mathbb{R}",ttype:CONST,notexcopy:!0},{input:"ZZ",tag:"mo",output:"ℤ",tex:"mathbb{Z}",ttype:CONST,notexcopy:!0},{input:"f",tag:"mi",output:"f",tex:null,ttype:UNARY,func:!0,val:!0},{input:"g",tag:"mi",output:"g",tex:null,ttype:UNARY,func:!0,val:!0},{input:"'",tag:"mo",output:"′",tex:null,ttype:CONST,notexcopy:!0,val:!0},{input:"''",tag:"mo",output:"′′",tex:null,ttype:CONST,notexcopy:!0,val:!0},{input:"'''",tag:"mo",output:"′′′",tex:null,ttype:CONST,notexcopy:!0,val:!0},{input:"''''",tag:"mo",output:"′′′′",tex:null,ttype:CONST,notexcopy:!0,val:!0},{input:"lim",tag:"mo",output:"lim",tex:null,ttype:UNDEROVER},{input:"Lim",tag:"mo",output:"Lim",tex:null,ttype:UNDEROVER},{input:"sin",tag:"mo",output:"sin",tex:null,ttype:UNARY,func:!0},{input:"cos",tag:"mo",output:"cos",tex:null,ttype:UNARY,func:!0},{input:"tan",tag:"mo",output:"tan",tex:null,ttype:UNARY,func:!0},{input:"arcsin",tag:"mo",output:"arcsin",tex:null,ttype:UNARY,func:!0},{input:"arccos",tag:"mo",output:"arccos",tex:null,ttype:UNARY,func:!0},{input:"arctan",tag:"mo",output:"arctan",tex:null,ttype:UNARY,func:!0},{input:"sinh",tag:"mo",output:"sinh",tex:null,ttype:UNARY,func:!0},{input:"cosh",tag:"mo",output:"cosh",tex:null,ttype:UNARY,func:!0},{input:"tanh",tag:"mo",output:"tanh",tex:null,ttype:UNARY,func:!0},{input:"cot",tag:"mo",output:"cot",tex:null,ttype:UNARY,func:!0},{input:"coth",tag:"mo",output:"coth",tex:null,ttype:UNARY,func:!0},{input:"sech",tag:"mo",output:"sech",tex:null,ttype:UNARY,func:!0},{input:"csch",tag:"mo",output:"csch",tex:null,ttype:UNARY,func:!0},{input:"sec",tag:"mo",output:"sec",tex:null,ttype:UNARY,func:!0},{input:"csc",tag:"mo",output:"csc",tex:null,ttype:UNARY,func:!0},{input:"log",tag:"mo",output:"log",tex:null,ttype:UNARY,func:!0},{input:"ln",tag:"mo",output:"ln",tex:null,ttype:UNARY,func:!0},{input:"abs",tag:"mo",output:"abs",tex:"text{abs}",ttype:UNARY,func:!0,notexcopy:!0},{input:"Sin",tag:"mo",output:"sin",tex:null,ttype:UNARY,func:!0},{input:"Cos",tag:"mo",output:"cos",tex:null,ttype:UNARY,func:!0},{input:"Tan",tag:"mo",output:"tan",tex:null,ttype:UNARY,func:!0},{input:"Arcsin",tag:"mo",output:"arcsin",tex:null,ttype:UNARY,func:!0},{input:"Arccos",tag:"mo",output:"arccos",tex:null,ttype:UNARY,func:!0},{input:"Arctan",tag:"mo",output:"arctan",tex:null,ttype:UNARY,func:!0},{input:"Sinh",tag:"mo",output:"sinh",tex:null,ttype:UNARY,func:!0},{input:"Sosh",tag:"mo",output:"cosh",tex:null,ttype:UNARY,func:!0},{input:"Tanh",tag:"mo",output:"tanh",tex:null,ttype:UNARY,func:!0},{input:"Cot",tag:"mo",output:"cot",tex:null,ttype:UNARY,func:!0},{input:"Sec",tag:"mo",output:"sec",tex:null,ttype:UNARY,func:!0},{input:"Csc",tag:"mo",output:"csc",tex:null,ttype:UNARY,func:!0},{input:"Log",tag:"mo",output:"log",tex:null,ttype:UNARY,func:!0},{input:"Ln",tag:"mo",output:"ln",tex:null,ttype:UNARY,func:!0},{input:"Abs",tag:"mo",output:"abs",tex:"text{abs}",ttype:UNARY,func:!0,notexcopy:!0},{input:"det",tag:"mo",output:"det",tex:null,ttype:UNARY,func:!0},{input:"exp",tag:"mo",output:"exp",tex:null,ttype:UNARY,func:!0},{input:"dim",tag:"mo",output:"dim",tex:null,ttype:CONST},{input:"mod",tag:"mo",output:"mod",tex:"text{mod}",ttype:CONST,notexcopy:!0},{input:"gcd",tag:"mo",output:"gcd",tex:null,ttype:UNARY,func:!0},{input:"lcm",tag:"mo",output:"lcm",tex:"text{lcm}",ttype:UNARY,func:!0,notexcopy:!0},{input:"lub",tag:"mo",output:"lub",tex:null,ttype:CONST},{input:"glb",tag:"mo",output:"glb",tex:null,ttype:CONST},{input:"min",tag:"mo",output:"min",tex:null,ttype:UNDEROVER},{input:"max",tag:"mo",output:"max",tex:null,ttype:UNDEROVER},{input:"uarr",tag:"mo",output:"↑",tex:"uparrow",ttype:CONST},{input:"darr",tag:"mo",output:"↓",tex:"downarrow",ttype:CONST},{input:"rarr",tag:"mo",output:"→",tex:"rightarrow",ttype:CONST},{input:"->",tag:"mo",output:"→",tex:"to",ttype:CONST},{input:"|->",tag:"mo",output:"↦",tex:"mapsto",ttype:CONST},{input:"larr",tag:"mo",output:"←",tex:"leftarrow",ttype:CONST},{input:"harr",tag:"mo",output:"↔",tex:"leftrightarrow",ttype:CONST},{input:"rArr",tag:"mo",output:"⇒",tex:"Rightarrow",ttype:CONST},{input:"lArr",tag:"mo",output:"⇐",tex:"Leftarrow",ttype:CONST},{input:"hArr",tag:"mo",output:"⇔",tex:"Leftrightarrow",ttype:CONST},AMsqrt,AMroot,AMfrac,AMdiv,AMover,AMsub,AMsup,{input:"hat",tag:"mover",output:"^",tex:null,ttype:UNARY,acc:!0},{input:"bar",tag:"mover",output:"¯",tex:"overline",ttype:UNARY,acc:!0},{input:"vec",tag:"mover",output:"→",tex:null,ttype:UNARY,acc:!0},{input:"tilde",tag:"mover",output:"~",tex:null,ttype:UNARY,acc:!0},{input:"dot",tag:"mover",output:".",tex:null,ttype:UNARY,acc:!0},{input:"ddot",tag:"mover",output:"..",tex:null,ttype:UNARY,acc:!0},{input:"ul",tag:"munder",output:"̲",tex:"underline",ttype:UNARY,acc:!0},AMtext,AMmbox,AMquote,{input:"color",tag:"mstyle",ttype:BINARY},{input:"bb",tag:"mstyle",atname:"fontweight",atval:"bold",output:"bb",tex:"mathbf",ttype:UNARY,notexcopy:!0},{input:"mathbf",tag:"mstyle",atname:"fontweight",atval:"bold",output:"mathbf",tex:null,ttype:UNARY},{input:"sf",tag:"mstyle",atname:"fontfamily",atval:"sans-serif",output:"sf",tex:"mathsf",ttype:UNARY,notexcopy:!0},{input:"mathsf",tag:"mstyle",atname:"fontfamily",atval:"sans-serif",output:"mathsf",tex:null,ttype:UNARY},{input:"bbb",tag:"mstyle",atname:"mathvariant",atval:"double-struck",output:"bbb",tex:"mathbb",ttype:UNARY,codes:AMbbb,notexcopy:!0},{input:"mathbb",tag:"mstyle",atname:"mathvariant",atval:"double-struck",output:"mathbb",tex:null,ttype:UNARY,codes:AMbbb},{input:"cc",tag:"mstyle",atname:"mathvariant",atval:"script",output:"cc",tex:"mathcal",ttype:UNARY,codes:AMcal,notexcopy:!0},{input:"mathcal",tag:"mstyle",atname:"mathvariant",atval:"script",output:"mathcal",tex:null,ttype:UNARY,codes:AMcal},{input:"tt",tag:"mstyle",atname:"fontfamily",atval:"monospace",output:"tt",tex:"mathtt",ttype:UNARY,notexcopy:!0},{input:"mathtt",tag:"mstyle",atname:"fontfamily",atval:"monospace",output:"mathtt",tex:null,ttype:UNARY},{input:"fr",tag:"mstyle",atname:"mathvariant",atval:"fraktur",output:"fr",tex:"mathfrak",ttype:UNARY,codes:AMfrk,notexcopy:!0},{input:"mathfrak",tag:"mstyle",atname:"mathvariant",atval:"fraktur",output:"mathfrak",tex:null,ttype:UNARY,codes:AMfrk}],AMnames=[],AMmathml="http://www.w3.org/1998/Math/MathML",AMnestingDepth,AMpreviousSymbol,AMcurrentSymbol,AMbody,AMnoMathML=!1,AMtranslated=!1,AMisGecko=0,AMnoFonts=!1;
if(AMinitSymbols(),AMisIE&&(document.write('<object id="mathplayer"  classid="clsid:32F66A20-7614-11D4-BD11-00104BD3F987"></object>'),document.write('<?import namespace="m" implementation="#mathplayer"?>')),"undefined"!=typeof window.addEventListener)window.addEventListener("load",generic,!1);else if("undefined"!=typeof document.addEventListener)document.addEventListener("load",generic,!1);else if("undefined"!=typeof window.attachEvent)window.attachEvent("onload",generic);else if("function"==typeof window.onload){var existing=onload;window.onload=function(){existing(),generic()}}else window.onload=generic;if(checkForMathML){checkForMathML=!1;var nd=AMisMathMLavailable();AMnoMathML=null!=nd}