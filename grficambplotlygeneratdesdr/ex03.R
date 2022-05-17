library(ggplot2)
library(plotly)
library(htmlwidgets)

arxSortida<-"L:\\David\\mcomas\\codapack\\grficambplotlygeneratdesdr\\ex07.html";

p  = ggplot(data=iris) + geom_point(aes(x = Sepal.Length, y = Sepal.Width, col = Species))

vp = ggplotly(p)

saveWidget(vp,arxSortida, selfcontained = FALSE, libdir ="libHTML")
