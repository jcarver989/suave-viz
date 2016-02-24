# Suave Viz

## Visualize any dataset in seconds
Suave Viz exists because there's a lack of good tools for exploratory data visualization. Existing tools require way too much effort to get a decent chart (ahem...ggplot) or trap you in some proprietary vendor's crappy dashboard UI.

Suave Viz solves that pain by giving you a dead-simple command-line tool for data visualization. Just pass some data in TSV format and Suave Viz opens a great looking, interactive chart in your browser. 

It's built on [Suave Charts](http://suavecharts.com) & D3.js.

## Is it any good?

Yes. It's fantastic. It's like having a freaking data lightsaber that's even easier to use than successfully trolling Apple fanboys. 

### What types of charts does it support?

- Line charts
- Bar charts
- Scatter plots
- Histograms

## Getting started

1. Download the Suave viz jar from: ...
2. Put the `suave` script on your path
3. Use it!

## Basic Usage

### Your first chart
Assume we have a TSV file of stock data: stocks.tsv
```
Date	Price
2016-01-01	10
2016-01-02	11
2016-01-03	12
2016-01-04	13
2016-01-05	14
```

Then to graph dates on the x-axis and prices on the y with the default line chart, we simply do:

```bash
suave stocks.tsv 
```

### Multiple series chart
And if there were multiple stocks we wanted to graph (multiple lines), we'd format the data like this:

stocks.tsv
```
Date	FBX	TWTR
2016-01-01	10	18
2016-01-02	11	17
2016-01-03	12	10
2016-01-04	13	5
2016-01-05	14	0
```

```
suave stocks.tsv
```

if we wanted a scatter plot instead, we'd simply do:
```bash
suave stocks.tsv --chart scatter
```

### Data Format
Suave Viz makes some assumptions about the data you pass it: 

- The data is in TSV format (tab separated columns) 
- The first line in the file is a header with names for each column (you can toggle this assumption)
- The first column in the file are your x-axis labels. These can be any text string
- Each column past the first is a series (ex separate line) of y-values you'd like to graph, they must be a number
- Histograms expect data with only a single column, every other chart type expects at least 2 columns (x and y)

### More Examples
Suave Viz supports input from stdin as well, so you can pipe data into it from anywhere.

#### Graph a SQL query

```bash
mysql -h mysql-server.com -e "use prod_db; SELECT SUM(price) from product_sales GROUP BY day;" | suave --chart bar
```

#### Graph the data from an API that returns TSV
```bash
curl http://some-api/some-data | suave --chart histogram 
```

## API Documentation

### Basic usage
```bash
 suave [input file] --option1 --option2 value
```

#### --chart [string]
##### available values: line, bar, scatter or histogram
##### default value: line
Sets the chart type to graph. With the exception of histogram, which expects a single input column, all chart types expect at least 2 columns (x and y values).

#### --no-header
##### default value: false
If your data does not include a header line (first line is the names of each column), set this flag.

#### --ticks [integer]
##### default: 10
Sets how many "ticks" to draw on the x-axis.

#### --dots
##### default: not enabled
If `--chart line` is selected, this option draws a circle that shows a tooltip on hover for each point in the chart.

#### --smooth
##### default: not enabled
If `--chart line` is selected, this smooths the chart lines. 

#### --dotSize [integer]
##### default: 6
If `--chart line` or `--chart scatter` is selected, this controls how big the circles for points are. 

### Histogram specific options:

#### --bins [integer]
##### default: 10
Sets how many evenly spaced buckets to use in the histogram. 

#### --domain [integer, integer]
Sets the range of the buckets to use, ex `--domain -10, 10` would render a histogram with buckets ranging from -10 to 10. By default Suave Viz will try to guess the domain based on your input data, this option allows finer grained control if you'd like to "zoom in" or "zoom out" on your histogram.  
