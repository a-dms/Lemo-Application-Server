(function(d3custom, $, undefined) {


  d3custom.run = function() {

	var w = 960,
    h = 600,
    amt = 24,
    maxPos = 11,
    fill = d3.scale.category20(),
    nodes = [],
    links = [],
    foci = [];
	
	
	var _nodes = d3custom.nodes,
		_links = d3custom.links;
	
	

	//check if we have values to work with
    if(!_nodes || !_links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }


	function init(){
		console.log("Bin im Init - Starting Node Init");
	   	var posCounter = 1;
		$.each(_nodes, function(i,v) {

			if(v.pathId <= amt){
				if (i >=1 && _nodes[i-1].pathId==_nodes[i].pathId) {
					posCounter++;
				} else {posCounter = 1; console.log("Neuer Pfad");}
				console.log("Schreibe Node "+i+" Pfad: "+v.pathId+" Position: "+posCounter+" Name:"+v.title);
		   		nodes.push({"pid":v.pathId , "pos":posCounter, "name":v.title, "value": v.value, "type": v.type, "totalRequests": v.totalRequests, "totalUsers":v.totalUsers, "selected":false});
			} else console.log("Max. number of pathes ("+amt+") reached ...!");
		});
		console.log("Bin im Init - Finished Node Init, starting Link Init " +_links.length);

	   	if(_links.length){
	   		console.log("Handle Links as Array");
			$.each(_links, function(i,v) {
				if(v.pathId <= amt){
					links.push({"source":nodes[v.source],"target":nodes[v.target],"value":v.value});
		   			 console.log("Schreibe Link: PathId "+nodes[v.source].pid+"("+v.source+"--"+v.target+")"+" Source: "+nodes[v.source].name+" Target: "+nodes[v.target].name);
				} else console.log("Max. number of pathes ("+amt+") reached ...!");
			});
	   	} else {
	   			console.log("Handle Links as Object");
	   			links.push({"source":nodes[_links.source],"target":nodes[_links.target],"value":_links.value});
	   		}
	   	console.log("Bin im Init - Finished Link Init .... Leaving");

	}

for (var i=0; i<amt; i++) {
	foci.push({x:20+920/(amt-1)*(i), y:20});
}


var vis = d3.select("#viz").append("svg:svg")
.attr("width", w)
.attr("height", h);

var force = d3.layout.force()
    .friction(.85)
    .gravity(0)
    .linkStrength(0)
    .charge(0)
    .size([w, h])
	.on("tick", function(e) {tick(e)});


	function tick(e) {
	  // Push nodes toward their designated focus.
	  var k = .1 * e.alpha;
	  nodes.forEach(function(o, i) {
	    o.y += (foci[24-o.pid].y - o.y + (o.pos-1)*380/maxPos + 20) * k;
	    o.x += (foci[24-o.pid].x - o.x) * k;
	  });


   vis.selectAll("g.node")
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"});

  vis.selectAll("line.link")
      .attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });
	};

   function greyout(color) {
        var newC = 0.3 * color.r + 0.6 * color.g + 0.1 * color.b;
        return d3.rgb(r=newC,g=newC,b=newC);
        }


 	nodes = force.nodes(),
	links = force.links();

 	init();

 	var pathAmount = [];

//for (var i=0; i<nodes2.length;i++) {
//
// 	console.log("Before sort "+nodes2[i].pfad+"/"+nodes2[i].pos);
// }



/*nodes2.sort(function(a, b){
	if(a.path < b.path)
		return -1;
	if (a.path > b.path && a.pos < b.pos)
		return 1;
	if (a.path = b.path && a.pos > b.pos)
		return 1;
	if (a.path = b.path && a.pos < b.pos)
		return -1;
	return 0;
})
*/
//for (var i=0; i<nodes2.length;i++) {
// 	console.log("After sort"+nodes2[i].pfad+"/"+nodes2[i].pos);
// }

//for(var j=0; j<nodes2.length; j++) {
//  nodes.push(nodes2[j]);
//}
//
//for (var j=1; j<nodes.length; j++) {
//       if (nodes[j-1].pfad==nodes[j].pfad) {
//       console.log("in");
//       links.push({source: nodes[j-1], target: nodes[j]});
//       }
//    }
 console.log(links.length);

/*for (var i=0; i<links.length;i++) {
 	console.log(""+links[i].source.pfad+"/"+links[i].target.pfad);
 }
*/
vis.selectAll("line.link")
      .data(links)
      .enter().append("svg:line")
      .attr("class", "link")
      .style("stroke-width","1")
      .style("stroke","#000");

      var node = vis.selectAll("g.node")
      .data(nodes, function(d) { return d.id;});

      node.selectAll("circle")
      .attr("r",8)
      .style("fill", function(d) { return fill(d.name); })
      .style("stroke", function(d) { return d3.rgb(fill(1)).darker(2); })
      .style("stroke-width", 1.5)

      node.selectAll("text")
      .text(function(d) {return d.name});

  var nodeEnter = node.enter().append("svg:g")
      .attr("class", "node");

  nodeEnter.append("circle")
       .attr("class", "node")
       .attr("r",8)

       .attr("title",function(d){return d.name;})
       .attr("cx", function(o) {return 0;})
       .attr("cy", function(o) { return 0;})
       .on("click", function(d) {


      if (d.selected == true) {
      for (var i=0; i<foci.length; i++)
      foci[i]=({x:20+920/(amt-1)*(i), y:20});
      d3.selectAll("g.node circle").style("fill", function(o, i) {
      return  fill(o.name);
      });
      
       d3.selectAll("g.node text").text(function(o, i) {
        return "";
       });
      force.start();
      }
      else {
      
      d3.selectAll("g.node circle").style("fill", function(o, i) {
        return o.pid != d.pid ? greyout(d3.rgb(fill(o.name))) : fill(o.name);
       });

       
      d3.selectAll("g.node text").text(function(o, i) {
        return o.pid != d.pid ? "" : o.name;
       });

      nodes.forEach(function(o, i) {
      if (o.pid == d.pid) {
    	  o.selected=true;
      } else o.selected=false;
      });

      for (var i=0; i<amt; i++) {
	     foci[i]={x:20+920/(amt-1)*(i), y:20};
      }


      var p = 24-d.pid;
      if (p>amt/2) {
      for (var i=0; i<p; i++){
      foci[i].x=20+660/(amt-1)*i;
      }
      for (var i=p; i<amt; i++) {
      foci[i].x=280+660/(amt-1)*i;
      }
      }
      else {
      for (var i=0;i<p+1;i++) {
      foci[i].x=20+660/(amt-1)*i;
      }
      for (var i=p+1; i<amt; i++) {
      foci[i].x=280+660/(amt-1)*i;
      }
      }
      force.start();
      }
      })
       .style("fill", function(d) { return fill(d.name); })
      .style("stroke", function(d) { return d3.rgb(fill(1)).darker(2); })
      .style("stroke-width", 1.5);

  nodeEnter.append("svg:text")
      .attr("class", "nodetext")
      .attr("dx", "1em")
      .attr("dy", ".3em")
      .text(function(d) { return ""});
      
      
nodeEnter.append("nodetitle")
.text(function(d) { return "<b>Support</b>: "+d.value});


force.start();



//$('#minSup2-slider').slider({
//
//	range: false,
//	min : 1,
//	max :  10,
//	value: 10,
//	slide : function( event, ui ) {
//		$( "#minSupSlider2-label" ).html( "Minimum Support (0.1 - 1): " + ui.value );
//
//
//		console.log("MinSup Value: "+ui.value);
//	}
//});
//


//Bind slide event to update minSup Value in front end
$( "#minSup-slider" ).bind( "slide", function(event, ui) {
	$( "#minSupSlider-label" ).html( "Minimum Support (0.1 - 1): " + ui.value/10 );
});

$('nodetitle').parent().tipsy({
    gravity: 'sw',
    html: true,
    title: function() { return $(this).find('nodetitle').text(); }
  });

  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);