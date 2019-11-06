/*
	Modal Object
	Create focused iframe object
	
	Bugs:
		IE size rendering issues (iframe position)
		Keyboard Focus excluded
*/

//	This is a cross-frame static class
var Modal = (function() {
	//	First step: Find Existing Class
	var m,w = window;
	while (w.parent != w && w.parent != null) {
		w = w.parent;
		if (w.Modal) m = w.Modal;
	}
	//	Second Step: Create the Class
	return m||(w.Modal = function() {
		var m = function(url,callback) {
			var d = Modal.count++;
			var n = "Modal_"+d;
			var el = new Element("iframe",{frameBorder:0,border:0,src:url,style:"top:0px;left:0px;z-index:"+(1000+d),"class":"modal"});
			el.style.position = (Modal.IEFixed)?"absolute":"fixed";
			el.fadeIn = function(cb) {
				if (!Fx.Styles) return cb();
				if (!this.fx) this.fx = new Fx.Styles(this,{duration:250,fps:30});
				this.fx.options.onComplete = cb;
				this.fx.options.transition = Fx.Transitions.quadIn;
				this.fx.custom({opacity:[0,1]});
			}
			el.fadeOut = function(cb) {
				if (!Fx.Styles) return cb();
				if (!this.fx) this.fx = new Fx.Styles(this,{duration:250,fps:30});
				this.fx.options.onComplete = cb;
				this.fx.options.transition = Fx.Transitions.quadIn;
				this.fx.custom({opacity:[1,0]});
			}
			el.close = function() {
				this.parentNode.removeChild(this);
				Modal.removeActive(this);
				Modal.Screen.check();
			}
			el.onreply = callback || function(data,submitted) {};
			el.cancel = function(data) {
				this.onreply(data,false);
				this.fadeOut(this.close.bind(this));
			}
			el.reply = function(data) {
				this.onreply(data,true);
				this.fadeOut(this.close.bind(this));
			}
			Event.observe(el,"load",Modal.load.bind(el));
			el.center = function() {
				//	Center Modal
				var p = document.body;
				var x = (p.offsetWidth - this.offsetWidth)/2;
				var y = (p.offsetHeight - this.offsetHeight)/2;
				if (!isNaN(x+y)) { 
					this.style.left = x+"px";
					this.style.top = y+"px";
				}
			}
			el.setSize = function(w,h) {
				var mW = window.innerWidth || document.body.offsetWidth || 0;
				var mH = window.innerHeight || document.body.offsetHeight || 0;
				if (w==-1 || h==-1) {
					var d = this.contentWindow.document;
					var b = d.getElementById("aspnetForm")|| d.documentElement || d.body;
					//	Auto-Size
					if (w==-1) w = b.scrollWidth;
					if (h==-1) h = b.scrollHeight;
				}
				if (isNaN(w)) this.style.width = w;
				else if (w>=0) this.style.width = Math.min(mW,w)+"px";
				if (isNaN(h)) this.style.height = h;
				else if (h>=0) this.style.height = Math.min(mH,h)+"px";
			}
			if (url) {
				el.src = url;
				el.style.visibility = "hidden";
				Modal.active.push(el);
				Modal.Screen.check();
				Modal.Loader.show();
			}
			var w = Modal.base;
			Event.observe(w,"resize",el.center.bind(el));
			if ((w.document.readyState != "complete")							//	Wait for Ready State...
			&&	(w.document.attachEvent != null)) {								//	...for IE only
				w.document.attachEvent("onreadystatechange",function() {
					if (w.document.readyState == "complete") w.document.body.appendChild(el);
				});
			} else w.document.body.appendChild(el);
			return el;
		}
		
		m.active = [];
		m.removeActive = function(modal) {
			var a = Modal.active;
			var iL = a.length;
			for (var i=0; i<iL; i++) if (a[i] == modal) {
				a.splice(i,1);
				break;
			}
		}
		
		m.IEFixed = (Prototype.Browser.IE && Prototype.Browser.Version<7);
		m.count = 0;
		
		m.base = (function() {
			var w = window;
			while (w.parent != w &&	w.parent != null) w = w.parent;
			return w;
		})();
		
		m.Screen = new Element("div",{id:"overlay",style:"display:none;position:"+(m.IEFixed?"absolute":"fixed")+";z-index:800;top:0px;left:0px;height:100%;width:100%;"});
		m.Screen.check = function() {
			if (Modal.active.length>0) {
				if (!this.parentNode) {
					if (document.readyState != "complete"					//	Avoid IE Load Failure
					&&	document.attachEvent != null) {
						document.attachEvent("onreadystatechange",function() {
							if (document.readyState == "complete") {
								document.detachEvent("onreadystatechange",arguments.callee);
								return Modal.Screen.check();
							}
						});
					} else document.body.appendChild(this);
				}
				if (this.style.display == "none") this.style.display = "block";
				if (Modal.IEFixed) document.body.style.overflow = "hidden";
				
				var a = Modal.active;
				var i = a.length-1;
				a[i].style.zIndex = 1000;
				while (--i > -1) a[i].style.zIndex = 700+i;
				
				Event.observe(document,"keypress",Modal.Esc);
				//Event.observe(window,"scroll",Event.stop);
			} else {
				if (this.style.display != "none") this.style.display = "none";
				if (Modal.IEFixed) document.body.style.overflow = "";
				Modal.Loader.hide();
				Event.stopObserving(document,"keypress",Modal.Esc);
				//Event.stopObserving(document.body,"scroll",Event.stop);
			}
		}
		
		m.Loader = new Element("div",{"class":"modalLoader",style:"position:"+(m.IEFixed?"absolute":"fixed")+";z-index:1200;display:none;"}).update("Loading");
		m.Loader.show = function() {
			if (!this.parentNode) {
				if (document.readyState != "complete"					//	Avoid IE Load Failure
				&&	document.attachEvent != null) {
					document.attachEvent("onreadystatechange",function() {
						if (document.readyState == "complete") {
							document.detachEvent("onreadystatechange",arguments.callee);
							return Modal.Loader.show();
						}
					});
				} else document.body.appendChild(this);
			}
			if (!this.center) this.center = function() {
				var p = document.body;
				var x = (p.offsetWidth - this.offsetWidth)/2;
				var y = (p.offsetHeight - this.offsetHeight)/2;
				if (!isNaN(x+y)) { 
					this.style.left = x+"px";
					this.style.top = y+"px";
				}
			}
			if (this.style.display == "none") {
				this.style.display = "block";
				this.center();
			}
		}
		m.Loader.hide = function() {
			if (this.style.display != "none") this.style.display = "none";
		}
		m.load = function() {
			this.center();
			Modal.Loader.hide();
			var c = this.contentWindow;
			if (this.style.visibility == "hidden") setTimeout(this.fadeIn.bind(this),10);
			Event.observe(c.document,"keypress",Modal.Esc.bind(this));
			Event.observe(c,"resize",this.center.bind(this));
		}
		m.Esc = function(e) {					// Modal Escape
			if (Modal.active.length) {
				var k = e?e.keyCode:window.event.keyCode;
				var esc = e?e.DOM_VK_ESCAPE||27:27;
				if (k == esc) {
					var a = Modal.active;
					a[a.length-1].cancel(null);
					return false;
				}
			}
		}
		return m;
	})();
})();