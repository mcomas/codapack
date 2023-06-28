cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
}
cdp_analysis = function(){  
  ################ MAIN #################
  graphname = sprintf("%s.pdf", tempfile())
  svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  vmeans = c()
  cmeans = palette()[3:2]
  if(exists("GROUP")){
    if(V1){
      GMEAN = apply(X, 2, function(x) tapply(x, GROUP, coda.base::gmean, na.rm=TRUE))
      vmeans = c(vmeans, "Geometric mean")
    }
    if(V2){
      AMEAN = apply(X, 2, function(x) tapply(x, GROUP, mean, na.rm=TRUE))
      vmeans = c(vmeans, "Arithmetic mean")
    }
    MAXCOL = 6
    vnames = colnames(X)
    vgroups = unique(GROUP)
    ngroups = length(vgroups)
    
    nr = ceiling(ncol(X) / MAXCOL)
    nc = ceiling(ncol(X) / nr)
    rng = range(X)
    
    cpar = par(no.readonly = TRUE)
    ml = matrix(c(rep(0, nc),
                  1:(nc*nr)), nrow = 1+nr, byrow = TRUE)
    if(V1 | V2){
      ml = ml+1
      layout(ml, heights = c(1, rep(3.5, nr)))
      par(mai=c(0,0,0,0))
      plot.new()
      legend(x="center", ncol=V1+V2,legend=vmeans,fill=cmeans[which(c(V1,V2))], title="")
    }else{
      layout(ml, heights = c(0.5, rep(3.5, nr)))
    }
    
    par(mai=c(1, 0.3, 0, 0.1))
    for(i in 1:ncol(X)){
      boxplot(X[,i]~GROUP, xlab = vnames[i], ylab = "", ylim = rng)
      hgap = (V1+V2-1) * 0.06
      if(V1) points(-hgap+1:nrow(GMEAN), GMEAN[,i], pch = 22, bg= cmeans[1], cex = 2)
      if(V2) points(+hgap+1:nrow(GMEAN), AMEAN[,i], pch = 22, bg = cmeans[2], cex = 2)
    }
    par(cpar)
  }else{
    if(V1){
      GMEAN = apply(X, 2, coda.base::gmean, na.rm=TRUE)
      vmeans = c(vmeans, "Geometric mean")
    }
    if(V2){
      AMEAN = apply(X, 2, mean, na.rm=TRUE)
      vmeans = c(vmeans, "Arithmetic mean")
    }
    cpar = par(no.readonly = TRUE)
    ml = matrix(0:1, nrow = 2, byrow = TRUE)
    if(V1 | V2){
      ml = ml+1
      layout(ml, heights = c(1, 3.5))
      par(mai=c(0,0,0,0))
      plot.new()
      legend(x="center", ncol=V1+V2,legend=vmeans,fill=cmeans[which(c(V1,V2))], title="")
    }else{
      layout(ml, heights = c(0.5, 3.5))
    }
    par(mai=c(1, 0.5, 0, 0.1))
    boxplot(X)
    hgap = (V1+V2-1) * 0.1
    if(V1) points(-hgap+1:ncol(X), GMEAN, pch = 22, bg= cmeans[1], cex = 2)
    if(V2) points(+hgap+1:ncol(X), AMEAN, pch = 22, bg = cmeans[2], cex = 2)
    par(cpar)
  }
  dev.off()
  
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = list()
  )
}