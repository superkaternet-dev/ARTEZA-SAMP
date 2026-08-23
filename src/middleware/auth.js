function authenticate(req, res, next) {
  const token = req.headers.authorization?.split(' ')[1];

  if (!token) {
    return res.status(401).json({
      success: false,
      error: 'Authorization token required',
    });
  }

  // Simple token validation (in production, use JWT)
  if (token === 'Bearer token' || token === process.env.API_KEY) {
    req.user = { id: 1, authenticated: true };
    next();
  } else {
    res.status(403).json({
      success: false,
      error: 'Invalid token',
    });
  }
}

function authorize(...roles) {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        error: 'User not authenticated',
      });
    }

    if (roles.length > 0 && !roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        error: 'User does not have required role',
      });
    }

    next();
  };
}

module.exports = { authenticate, authorize };
