cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if( (!exists('Y') & !exists('GROUP')) ) return("No response has been selected")
  
  if(exists('GROUP')){
    if(length(unique(GROUP)) == 1) return("Response must have at least two categories")
    if(length(unique(GROUP)) > 2) return("More than two group no implemented yet")
  }
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  # cond2 = nrow(BasisX) == ncol(X)
  # if(!cond2) return("Basis not defined")
  
  # cond3 = NCOL(Y) == 1
  # if(!cond3) return("Only one numeric response variable")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_lasso_regularization.RData")
  # load("Rscripts/cdp_lasso_regularization.RData")
  W = NULL
  Y = GROUP
  PF = rep(1, ncol(X))
  if(exists("GROUP")){
    K = length(unique(GROUP))
    Y = as.numeric(factor(GROUP)) - 1
  }else{ 
    K = ncol(Y)
  }
  if(V1 == 'L1-coda'){
    Z = coda.base::coordinates(X, 'clr')
    
    H = cbind(lX, m_ = rowSums(lX))
    PF = c(rep(1, ncol(X)), 0)
  }
  if(V1 == 'L1-clr'){
    H = coda.base::coordinates(X, basis = 'clr')
  }
  if(V1 == 'L1-plr'){
    Y = rbind(0, Y)
    H = rbind(1, log(X))
    W = c(1000, rep(1, nrow(X)))
  }
  if(K == 1){
    GLMNET = glmnet::glmnet(H, Y, intercept = FALSE, weights = W, penalty.factor = PF)
  }else{
    if(K == 2){
      GLMNET = glmnet::glmnet(H, Y, family = 'binomial', weights = W, penalty.factor = PF)
    }else{
      GLMNET = glmnet::glmnet(H, Y, intercept = FALSE, family = 'multinomial', weights = W, penalty.factor = PF)
    }
  }

  if(V1 == 'L1-coda'){
    BETAS = t(as.matrix(GLMNET$beta))
    BETAS = BETAS[,-ncol(BETAS)]
    BETAS = BETAS - rowMeans(BETAS)
    LAMBDA = data.frame(lambda = GLMNET$lambda)
    COEFS = cbind(LAMBDA, as.data.frame(BETAS))
  }
  

  
  graphname <- sprintf("%s.svg", tempfile())
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  x = log(GLMNET$lambda)
  matplot(x, BETAS, type = 'l', lwd = 1, xlab = 'log(lambda)', ylab = 'clr-coefficients')
  dev.off()

  text_output = ""

  
  
  cdp_out = list(
    'text' = list(text_output),
    'dataframe' = list('coefficients' = COEFS),
    'graph' = graphname,
    'new_data' = NULL #new_data
  )
  # if(exists('X_new')){
  #   cdp_out[['new_data2']] = new_data2
  # }
  cdp_out
  
}