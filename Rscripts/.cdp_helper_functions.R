cdp_check_compositional = function(X, zero_allowed = FALSE, na_allowed = FALSE){
  cond1 = ncol(X) >= 2
  if(!cond1) return("At least two parts should be selected")
  minX = min(X)
  if(!na_allowed & is.na(minX)){
    ind = which(rowSums(is.na(X)) > 0)
    return(sprintf("Observation %s with missing data", paste(ind, collapse=',')))
  }
  if(na_allowed) minX = min(X, na.rm = TRUE)
  
  cond2 = minX > 0
  if(zero_allowed) cond2 = minX >= 0
  
  if(!cond2 & !zero_allowed) return("All parts should be positive")
  if(!cond2 & zero_allowed) return("No negative parts are allowed")
  
}
cdp_print_sbp = function(sbp, pnames){
  nccol = pmax(3, nchar(pnames))
  text_output = c("Partition:", capture.output({
    cat(sprintf(sprintf("%%%ds", nccol), pnames), "\n")
    cat(apply(matrix(sprintf(sprintf("%%%dd", nccol), sbp), byrow = TRUE, ncol = length(pnames)),
              1,
              paste, collapse=' '), sep='\n')
  }))
}
