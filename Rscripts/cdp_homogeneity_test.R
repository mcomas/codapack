cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('GROUP')) return("No group has been selected")
  if(length(unique(GROUP)) <= 1) return("Two or more categories are needed")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  p = ncol(X) - 1
  if(p < 1) return("At least two parts should be selected")
  
}

cdp_analysis = function(){
  # save.image("Rscripts/cdp_homogeneity_test.RData")
  
  B = as.matrix(coda.base::ilr_basis(ncol(X)))
  H = coda.base::coordinates(X, basis = B)
  colnames(H) = paste0('olr.', 1:ncol(H))
  
  complete = rowSums(is.na(H)) == 0 & !is.na(GROUP)
  H = H[complete,,drop = FALSE]
  G = as.factor(GROUP[complete])
  
  p = ncol(H)
  groups = levels(G)
  k = length(groups)
  n = as.vector(table(G))
  N = sum(n)
  
  group_name = "GROUP"
  if(exists("GROUP_NAME")) group_name = GROUP_NAME
  output = c("Box-M test for homogeneity of covariance matrices",
             paste("Composition:", paste(colnames(X), collapse = ", ")),
             paste("Group:", group_name))
  if(sum(!complete) > 0){
    output = c(output, sprintf("(%d observations deleted due to missingness)", sum(!complete)))
  }
  
  if(k <= 1){
    output = c(output, "Two or more non-empty groups are needed after removing missing data.")
    return(list('text' = output, 'dataframe' = list(), 'graph' = list(), 'new_data' = list()))
  }
  
  if(any(n <= p)){
    output = c(output, sprintf("Each group must have more observations than OLR coordinates (%d).", p))
    return(list('text' = output, 'dataframe' = list(), 'graph' = list(), 'new_data' = list()))
  }
  
  covariances = lapply(groups, function(gr){
    cov(H[G == gr,,drop = FALSE])
  })
  logdets = sapply(covariances, function(S){
    determinant(S, logarithm = TRUE)$modulus[1]
  })
  
  if(any(!is.finite(logdets))){
    output = c(output, "Box-M test could not be computed because at least one covariance matrix is singular.")
    return(list('text' = output, 'dataframe' = list(), 'graph' = list(), 'new_data' = list()))
  }
  
  pooled = Reduce('+', Map(function(S, ni) (ni - 1) * S, covariances, n)) / (N - k)
  pooled_logdet = determinant(pooled, logarithm = TRUE)$modulus[1]
  
  if(!is.finite(pooled_logdet)){
    output = c(output, "Box-M test could not be computed because the pooled covariance matrix is singular.")
    return(list('text' = output, 'dataframe' = list(), 'graph' = list(), 'new_data' = list()))
  }
  
  M = (N - k) * pooled_logdet - sum((n - 1) * logdets)
  correction = ((2 * p^2 + 3 * p - 1) / (6 * (p + 1) * (k - 1))) *
    (sum(1 / (n - 1)) - 1 / (N - k))
  chisq = M * (1 - correction)
  df = p * (p + 1) * (k - 1) / 2
  pvalue = pchisq(chisq, df = df, lower.tail = FALSE)
  
  result = data.frame(
    "Statistic" = chisq,
    "df" = df,
    "p.value" = pvalue,
    check.names = FALSE
  )
  
  group_sizes = data.frame(
    "Group" = groups,
    "Size" = n,
    check.names = FALSE
  )
  
  output = c(output, "", capture.output(print(result, row.names = FALSE)))
  output = gsub("[‘’]", "'", output)
  
  list(
    'text' = output,
    'dataframe' = list('boxm' = result, 'group.sizes' = group_sizes),
    'graph' = list(),
    'new_data' = list()
  )
}
