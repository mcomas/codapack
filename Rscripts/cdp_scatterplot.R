cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond3 = NCOL(X) >= 2
  if(!cond3) return("Select two or more variables")
  
}
cdp_analysis = function(){
  # save.image('Rscripts/cdp_scatterplot.RData')
  ################# MAIN #################
  graphname = sprintf("%s.pdf", tempfile())
  svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  
  if(exists("GROUP")){
    iGROUP = as.integer(factor(GROUP))
    vgroups = unique(GROUP)
    ngroups = length(vgroups)
    cgroups = palette()[1+1:ngroups]
    if(ncol(X) > 2){
      cpar = par(no.readonly = TRUE)
      pairs(X, bg = cgroups[iGROUP], pch = 22, oma=c(4,4,6,12))
      par(xpd=TRUE)
      legend(x="topright", bty = 'n', legend=vgroups,fill=cgroups, title="")
      par(cpar)
    }else{
      plot(X, bg = cgroups[iGROUP], pch = 22)
      legend("topright", bty = 'n', legend=vgroups,fill=cgroups, title="")
    }
    
  }else{
    if(ncol(X) > 2){
      pairs(X, bg = 1, pch = 22)
    }else{
      plot(X, bg = 1, pch = 22)
    }
  }
  
  dev.off()
  
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = list()
  )
}