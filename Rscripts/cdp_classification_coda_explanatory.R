cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if( (!exists('Y') & !exists('GROUP')) ) return("No response has been selected")
  
  if(exists('GROUP')){
    if(length(unique(GROUP)) == 1) return("Response must have at least two categories")
    if(length(unique(GROUP)) > 2 & !requireNamespace("nnet", quietly = TRUE)) return("Package nnet is required for multinomial logistic regression")
  }else{
    if(NCOL(Y) < 2) return("Response must be a count matrix with at least two columns")
    if(NCOL(Y) > 2 & !requireNamespace("nnet", quietly = TRUE)) return("Package nnet is required for multinomial logistic regression")
    if(!is.numeric(as.matrix(Y))) return("Response must be numeric counts")
    if(any(!is.finite(as.matrix(Y)))) return("Response must contain finite counts")
    if(any(as.matrix(Y) < 0)) return("Response counts must be non-negative")
    if(any(rowSums(as.matrix(Y)) == 0)) return("Response counts must have positive row totals")
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
  response_from_group = exists('GROUP')
  if(response_from_group){
    K = length(unique(GROUP))
    response_names = unique(GROUP)
    str_x = paste(colnames(H), collapse='+')
    
    if(K == 2){
      LABEL = unique(GROUP)[2]
      
      str_y = "y"
      str_frm = sprintf("%s ~ %s", str_y, str_x)
      FAMILY = 'binomial'
      model_type = 'binomial_group'
      
      Y_GLM = cbind(y = GROUP == LABEL)
      . = as.data.frame(cbind(Y_GLM,H))
      MOD = eval(parse(text = sprintf("glm(%s, ., family = '%s')", str_frm, FAMILY)))
    }else{
      str_y = "GROUP"
      str_frm = sprintf("%s ~ %s", str_y, str_x)
      model_type = 'multinomial_group'
      
      . = as.data.frame(H)
      .$GROUP = factor(GROUP, levels = response_names)
      MOD = eval(parse(text = sprintf("nnet::multinom(%s, data = ., trace = FALSE)", str_frm)))
      response_names = MOD$lev
    }
  }else{
    response_names = colnames(Y)
    str_y = sprintf("cbind(%s)", paste(colnames(Y), collapse=','))
    str_x = paste(colnames(H), collapse='+')
    str_frm = sprintf("%s ~ %s", str_y, str_x)
    
    . = as.data.frame(cbind(Y,H))
    if(NCOL(Y) == 2){
      FAMILY = 'binomial'
      model_type = 'binomial_counts'
      MOD = eval(parse(text = sprintf("glm(%s, ., family = '%s')", str_frm, FAMILY)))
    }else{
      model_type = 'multinomial_counts'
      MOD = eval(parse(text = sprintf("nnet::multinom(%s, data = ., trace = FALSE)", str_frm)))
    }
  }
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
  if(model_type %in% c('multinomial_group', 'multinomial_counts')){
    probs = predict(MOD, type = 'probs')
    matplot(probs, type = 'l', lty = 1, lwd = 1,
            xlab = 'Observation', ylab = 'Fitted probability',
            ylim = c(0,1))
    legend('topright', legend = colnames(probs), col = seq_len(ncol(probs)),
           lty = 1, lwd = 1, bty = 'n')
  }else{
    oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
    plot(MOD,sub.caption=deparse(as.formula(MOD)))  # Plot the model information
    par(oldpar)
  }
  dev.off()
  
  
  new_data  <-  list()
  if(V1){ # fitted values
    if(model_type %in% c('multinomial_group', 'multinomial_counts')){
      dpred = data.frame(predict(MOD, type = 'probs'))
    }else{
      probs = predict(MOD, type = 'response')
    }
    if(model_type == 'binomial_group'){
      dpred = data.frame(1-probs,probs)
    }
    if(model_type == 'binomial_counts'){
      dpred = data.frame(probs,1-probs)
    }
    names(dpred) = sprintf("%s.p", response_names)
    new_data = dpred
  }
  
  text_output = cdp_print_sbp(BasisX, colnames(X))
  text_output = c(text_output, capture.output(summary(MOD)))
  text_output = gsub("[‘’]", "'", text_output)
  
  if(model_type %in% c('multinomial_group', 'multinomial_counts')){
    COEFS = cbind(
      data.frame("Response" = row.names(coef(MOD))),
      as.data.frame(coef(MOD), row.names = FALSE)
    )
  }else{
    COEFS = MOD$coefficients
  }
  
  # if(exists('X_new')){
  #   H_new = coda.base::coordinates(X_new, coda.base::sbp_basis(BasisX))
  #   colnames(H_new) = paste0('olr.', 1:ncol(H_new))
  #   new_data2 = data.frame(predict(LM, newdata = as.data.frame(H_new)))
  #   names(new_data2) = paste0(colnames(Y), '.pred')
  # }
  # Ooutput
  cdp_out = list(
    'text' = list(text_output),
    'dataframe' = list('coefficients' = COEFS),
    'graph' = graphname,
    'new_data' = new_data
  )
  # if(exists('X_new')){
  #   cdp_out[['new_data2']] = new_data2
  # }
  cdp_out
  
}
