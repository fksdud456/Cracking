<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<!DOCTYPE html>
<html>
<head>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script><script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/series-label.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>

<style>
.button {
  display: inline-block;
  border-radius: 4px;
  background-color: #75357e;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 18px;
  padding: 10px;
  width: 200px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 3px;
}

.button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.button:hover span {
  padding-right: 25px;
}

.button:hover span:after {
  opacity: 1;
  right: 0;
}

</style>
</head>


<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>

<body>
<div id="wrapper">
<button class="button" style="vertical-align:middle" onClick="history.go(0)"><span>New Session</span></button>
</div>

<div id="container"></div>
<script>
function display(wer){
	 
	Highcharts.chart('container', {
	    title: {
	        text: 'Amount of Usage Motion Sensor from SmartPhone '
	    },
	    
	    subtitle: {
	    	text: 'This information comes from CONN table'
	    },

	    
	    yAxis: {
	        title: {
	            text: 'Count of usage'
	        }
	    },
	    
	    xAxis: {
	        categories: ['B7', 'B6', 'B5', 'B4', 'B3', 'B2', 'B1', 'NOW']
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	   
  	  
	    series: wer,

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }
	});
	
}

 $(document).ready(function(){	 
   $.ajax({ url:'dataChart.do?comm=w',
      success:function(data){  
    	 
    	  display(data); 
      },
      error:function(){
         alert('data1 fail');
      }
	});
}); 

</script>
</body>
</html>