cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = is.numeric(V1) && is.numeric(V2) && 1 < V1 && V1 <= V2
  if(!cond2) return("Minimum and maximum should define a range and be higher than one")
  
}
cdp_analysis = function(){
  
  library(fpc)
  library(cluster)
  
  ################# MAIN #################
  H <- coda.base::coordinates(X)
  
  i.nona = which(rowSums(is.na(H))==0)
  
  H.nona = H[i.nona,]
  
  krange = seq(V1, V2)
  l_km = lapply(krange, function(k){
    set.seed(123456789)
    kmeans(H.nona, k, nstart = 25)
  })
  
  v_calinski = sapply(l_km, function(km) fpc::calinhara(H.nona, km$cluster))
  dist.X.nona = dist(H.nona)
  v_silhouette = sapply(l_km, function(km) mean(cluster::silhouette(km$cluster, dist.X.nona)[,'sil_width']))
  
  
  if(V3 == 'Calinski index'){ # Calinski
    i_best = which.max(v_calinski)
  }else{ # Silhoutte
    D.X.nona = 
      i_best = which.max(v_silhouette)
  }
  
  km_best = l_km[[i_best]]
  
  graphname = NULL
  if(V1 != V2){
    graphname = sprintf("%s.svg", tempfile())
    svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
    cpar = par(mfrow = c(1,2))
    plot(krange, v_silhouette, type="l", xlab='Number of clusters', ylab='Average Silhouette', frame=FALSE)
    plot(krange, v_calinski, type="l", xlab='Number of clusters', ylab='Calinski index', frame=FALSE)
    par(cpar)
    dev.off()
  }
  
  gr = rep('na', nrow(X))
  gr[i.nona] = km_best$cluster
  df = data.frame(gr)
  colnames(df) = V4
  
  dres = data.frame('Centers' = krange,
                    'CH index' = v_calinski,
                    'AS index' = v_silhouette)
  
  dcenters = as.data.frame(coda.base::composition(km_best$centers, coda.base::basis(H)))
  names(dcenters) = colnames(X)
  
  dcenters$Size = km_best$size
  dcenters$`Within SS` = km_best$withinss
  
  output_text = list(capture.output(dres),
                     c("\nCenters:", capture.output(dcenters)),
                     sprintf("(between_SS / total_SS =  %.2f %%)", 100* km_best$betweenss / km_best$totss))
  
  if(nrow(H) - length(i.nona) > 0){
    output_text = c(output_text, 
                    sprintf("(%d observations deleted due to missingness)", nrow(H) - length(i.nona)))
  }
  
  # Output
  list(
    'text' = output_text,
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = df
  )
}
