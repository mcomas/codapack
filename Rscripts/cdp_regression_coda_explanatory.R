cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('Y')) return("No response has been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = nrow(BasisX) == ncol(X)
  if(!cond2) return("Basis not defined")
  
  cond3 = NCOL(Y) == 1
  if(!cond3) return("Only one numeric response variable")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_regression_coda_explanatory.RData")
  
  H = coda.base::coordinates(X, basis = coda.base::sbp_basis(BasisX))
  colnames(H) = paste0('olr.', 1:ncol(H))
  
  str_y = colnames(Y)
  if(ncol(Y) > 1) str_y = sprintf("cbind(%s)", paste(colnames(Y), collapse=','))
  str_x = paste(colnames(H), collapse='+')
  str_frm = sprintf("%s ~ %s", str_y, str_x)
  
  . = as.data.frame(cbind(Y,H))
  LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))
  
  #summary(LM)
  
  
  graphname <- sprintf("%s.svg", tempfile())
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
  plot(LM,sub.caption=deparse(as.formula(LM)))  # Plot the model information
  par(oldpar)
  dev.off()
  
  
  new_data  <-  list()
  if(V1) new_data[['residuals']] = LM$residuals
  if(V2) new_data[['fitted.values']] = LM$fitted.values
  
  text_output = cdp_print_sbp(BasisX, colnames(X))
  # nccol = pmax(3, nchar(colnames(X)))
  # text_output = c("Partition:", capture.output({
  # cat(sprintf(sprintf("%%%ds", nccol), colnames(X)), "\n")
  # cat(apply(matrix(sprintf(sprintf("%%%dd", nccol), BasisX), byrow = TRUE, ncol = ncol(X)),
  #       1,
  #       paste, collapse=' '), sep='\n')
  # }))
  text_output = c(text_output, capture.output(summary(LM)))
  text_output = gsub("[‘’]", "'", text_output)
  
  # Ooutput
  list(
    'text' = list(text_output),
    'dataframe' = list('coefficients' = LM$coefficients),
    'graph' = graphname,
    'new_data' = new_data
  )
  
}