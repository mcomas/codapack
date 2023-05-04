library(ggplot2)
library(plotly)
library(htmlwidgets)

p  = ggplot(data=iris) +
  geom_point(aes(x = Sepal.Length, y = Sepal.Width, col = Species))

vp = ggplotly(p)

saveWidget(vp, "ex02.html")

