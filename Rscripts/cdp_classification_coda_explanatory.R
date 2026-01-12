cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if( (!exists('Y') & !exists('GROUP')) ) return("No response has been selected")
  
  if(exists('GROUP')){
    if(length(unique(GROUP)) == 1) return("Response must have at least two categories")
    if(length(unique(GROUP)) > 2) return("More than two group no implemented yet")
  }
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = nrow(BasisX) == ncol(X)
  if(!cond2) return("Basis not defined")
  
  # cond3 = NCOL(Y) == 1
  # if(!cond3) return("Only one numeric response variable")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_classification_coda_explanatory.RData")
  # load("Rscripts/cdp_classification_coda_explanatory.RData")
  H = coda.base::coordinates(X, basis = coda.base::sbp_basis(BasisX))
  colnames(H) = paste0('olr.', 1:ncol(H))
  K = length(unique(GROUP))
  if(K == 2){
    LABEL = unique(GROUP)[2]
    
    str_y = "y"
    str_x = paste(colnames(H), collapse='+')
    str_frm = sprintf("%s ~ %s", str_y, str_x)
    FAMILY = 'binomial'
    
    Y = cbind(y = GROUP == LABEL)
    . = as.data.frame(cbind(Y,H))
    GLM = eval(parse(text = sprintf("glm(%s, ., family = '%s')", str_frm, FAMILY)))
  }else{ K > 2
    
  }
  # m1 = nnet::multinom(GROUP~., data = .[,-1],
  #                     maxit  = 10000,      # més iteracions
  #                     abstol = 1e-20,    # tolerància absoluta
  #                     reltol = 1e-20,    # tolerància relativa
  #                     trace  = FALSE)
  # m2 = glm((y=='North')~., data = dat, family = 'binomial',
  #          control = glm.control(
  #            epsilon = 1e-12,  # criteri de convergència (default = 1e-8)
  #            maxit   = 100     # iteracions màximes (default = 25)
  #          ))
  # m1
  # m2
  # summary(mod)
  # str_y = colnames(Y)
  # if(ncol(Y) > 1) str_y = sprintf("cbind(%s)", paste(colnames(Y), collapse=','))
  # 
  
  
  
  #summary(LM)
  
  
  graphname <- sprintf("%s.svg", tempfile())
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
  plot(GLM,sub.caption=deparse(as.formula(GLM)))  # Plot the model information
  par(oldpar)
  dev.off()
  
  
  new_data  <-  list()
  if(V1){ # fitted values
    if(K == 2){
      probs = predict(GLM, type = 'response')
      dpred = data.frame(1-probs,probs)
      names(dpred) = sprintf("%s.p", unique(GROUP))
      new_data = dpred
    }
  }
  
  text_output = cdp_print_sbp(BasisX, colnames(X))
  text_output = c(text_output, capture.output(summary(GLM)))
  text_output = gsub("[‘’]", "'", text_output)
  
  # if(exists('X_new')){
  #   H_new = coda.base::coordinates(X_new, coda.base::sbp_basis(BasisX))
  #   colnames(H_new) = paste0('olr.', 1:ncol(H_new))
  #   new_data2 = data.frame(predict(LM, newdata = as.data.frame(H_new)))
  #   names(new_data2) = paste0(colnames(Y), '.pred')
  # }
  # Ooutput
  cdp_out = list(
    'text' = list(text_output),
    'dataframe' = list('coefficients' = GLM$coefficients),
    'graph' = graphname,
    'new_data' = new_data
  )
  # if(exists('X_new')){
  #   cdp_out[['new_data2']] = new_data2
  # }
  cdp_out
  
}